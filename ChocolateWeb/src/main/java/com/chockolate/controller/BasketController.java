package com.chockolate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.chockolate.exception.ServiceException;
import com.chockolate.service.ProductService;
import com.chockolate.service.impl.BasketServiceImpl;



@Controller
public class BasketController {
	
	private ProductService productService;
	private BasketServiceImpl basketServiceImpl;

	 @Autowired
	    public BasketController(BasketServiceImpl basketServiceImpl, ProductService productService) {
	        this.basketServiceImpl = basketServiceImpl;
	        this.productService = productService;
	    }

	@GetMapping("/basket")
    public ModelAndView basket() {
        ModelAndView modelAndView = new ModelAndView("/basketPage");
        modelAndView.addObject("products", basketServiceImpl.getProductsInBasket());
        modelAndView.addObject("total", basketServiceImpl.getTotal().toString());
        return modelAndView;
    }
	
	@GetMapping("/addInBasket/{id}")
	public String addProductInBasket(@PathVariable String id, Model model) {
		try {
			basketServiceImpl.addProduct(productService.loadFindProductById(Long.parseLong(id)));
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "redirect:/catalog";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteProductInBasket(@PathVariable String id, Model model) {
		try {
			basketServiceImpl.removeProduct(productService.loadFindProductById(Long.parseLong(id)));
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "redirect:/basket";
	}
	
	@GetMapping("/order")
    public ModelAndView OrderPage() {
        ModelAndView modelAndView = new ModelAndView("/orderPage");
        modelAndView.addObject("products", basketServiceImpl.getProductsInBasket());
        String total = String.format("%.2f",basketServiceImpl.getTotal().toString());
        modelAndView.addObject("total", total);
        return modelAndView;
    }
}

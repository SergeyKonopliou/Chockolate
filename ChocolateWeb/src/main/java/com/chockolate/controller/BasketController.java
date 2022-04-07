package com.chockolate.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Order;
import com.chockolate.model.Product;
import com.chockolate.service.ProductService;
import com.chockolate.service.impl.BasketServiceImpl;
import com.chockolate.service.impl.OrderServiceImpl;
import com.chockolate.service.impl.UserDetailsServiceImpl;


/**
 * Класс,реагирующий на адреса связанные с корзиной и заказами
 */
@Controller
public class BasketController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private BasketServiceImpl basketServiceImpl;
	@Autowired
	private OrderServiceImpl orderServiceImpl;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	 /**
	  * Метод отображения страницы корзины с добавленными в неё продуктами
	  */
	@GetMapping("/basket")
    public ModelAndView basket() {
        ModelAndView modelAndView = new ModelAndView("/basketPage");
        modelAndView.addObject("products", basketServiceImpl.getProductsInBasket());
        String total = String.format("%.2f",basketServiceImpl.getTotal());
        modelAndView.addObject("total", total);
        modelAndView.addObject("userName", userDetailsServiceImpl.getEntryUser());
        modelAndView.addObject("cart", basketServiceImpl.getProductsInBasket());
        return modelAndView;
    }
	
	 /**
	  * Метод отображения страницы принятых заказов для админа
	  */
	@GetMapping("/orderslist")
    public String showOrdersListPage(Model model) {
        try {
        	model.addAttribute("orders", orderServiceImpl.loadAllOrder());
        	model.addAttribute("userName", userDetailsServiceImpl.getEntryUser());
        	model.addAttribute("cart", basketServiceImpl.getProductsInBasket());
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
        
        return "ordersListPage";
    }
	 /**
	  * Метод для удаления заказа из базы данных по id
	  */
	@GetMapping("/deleteOrder/{id}")
	public String deleteProductById(@PathVariable String id, Model model) {
		try {
			orderServiceImpl.deleteOrder(Long.parseLong(id));
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "redirect:/orderslist";
	}
	 /**
	  * Метод добавления продукта в корзину
	  */
	@GetMapping("/addInBasket/{id}")
	public String addProductInBasket(@PathVariable String id, Model model) {
		try {
			basketServiceImpl.addProduct(productService.loadProductById(Long.parseLong(id)));
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "redirect:/catalog";
	}
	
	 /**
	  * Метод удаления из корзины продукта по id
	  */
	@GetMapping("/delete/{id}")
	public String deleteProductInBasket(@PathVariable String id, Model model) {
		try {
			basketServiceImpl.removeProduct(productService.loadProductById(Long.parseLong(id)));
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "redirect:/basket";
	}
	 /**
	  * Метод отображения страницы для ввода информации о заказчике для сохранения заказа
	  */
	@GetMapping("/order")
    public ModelAndView OrderPage() {
        ModelAndView modelAndView = new ModelAndView("/orderPage");
        Map<Product, Integer> products = basketServiceImpl.getProductsInBasket();
        StringBuilder str = new StringBuilder();
        for(Map.Entry<Product, Integer> item : products.entrySet()){
            str.append( item.getKey().getName()).append("-");
            str.append( item.getValue()).append(" ");
        }
        String total = String.format("%.2f",basketServiceImpl.getTotal());
        modelAndView.addObject("order", new Order());
        modelAndView.addObject("orderList", str);
        modelAndView.addObject("products", products);
        modelAndView.addObject("total", total);
        modelAndView.addObject("userName", userDetailsServiceImpl.getEntryUser());
        modelAndView.addObject("cart", basketServiceImpl.getProductsInBasket());
        return modelAndView;
    }
	 /**
	  * Метод сохранения заказа
	  */
	@PostMapping("/saveOrder")
	public String saveOrder(@ModelAttribute("orderForm") @Valid Order order,BindingResult result,Model model,RedirectAttributes redirectAttributes) {
		if (!result.hasErrors()) {
			try {
				orderServiceImpl.addNewOrder(order);
				basketServiceImpl.clear();
				redirectAttributes.addFlashAttribute("addmessage", true);
			} catch (ServiceException e) {
				model.addAttribute("message", e.getMessage());
				return "error";
			}
		}else {	
			List<ObjectError> list = result.getAllErrors();
			redirectAttributes.addFlashAttribute("errors", list);
			redirectAttributes.addFlashAttribute("err", true);
			return "redirect:/order";
		} 
		return "redirect:/basket";
	}
}

package com.chockolate.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Order;
import com.chockolate.model.Product;
import com.chockolate.service.ProductService;
import com.chockolate.service.impl.BasketServiceImpl;
import com.chockolate.service.impl.OrderServiceImpl;


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

	 /**
	  * Метод отображения страницы корзины с добавленными в неё продуктами
	  */
	@GetMapping("/basket")
    public ModelAndView basket() {
        ModelAndView modelAndView = new ModelAndView("/basketPage");
        modelAndView.addObject("products", basketServiceImpl.getProductsInBasket());
        String total = String.format("%.2f",basketServiceImpl.getTotal());
        if(basketServiceImpl.getProductsInBasket().isEmpty()) {
        	 modelAndView.addObject("addmessage", true);
        }
        modelAndView.addObject("total", total);
        return modelAndView;
    }
	
	 /**
	  * Метод отображения страницы принятых заказов для админа
	  */
	@GetMapping("/orderslist")
    public String showOrdersListPage(Model model) {
        try {
        	model.addAttribute("orders", orderServiceImpl.loadAllOrder());
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
			basketServiceImpl.addProduct(productService.loadFindProductById(Long.parseLong(id)));
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
			basketServiceImpl.removeProduct(productService.loadFindProductById(Long.parseLong(id)));
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
        return modelAndView;
    }
	 /**
	  * Метод сохранения заказа
	  */
	@PostMapping("/saveOrder")
	public String saveOrder(@ModelAttribute @Valid Order order,BindingResult result,Model model) {
		if (!result.hasErrors()) {
			try {
				orderServiceImpl.addNewOrder(order);
				basketServiceImpl.clear();
				model.addAttribute("addmessage",true);
			} catch (ServiceException e) {
				model.addAttribute("message", e.getMessage());
				return "error";
			}
		}else {			
			return "redirect:/order";
		} 
		return "redirect:/basket";
	}
}

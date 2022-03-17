package com.chockolate.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Product;
import com.chockolate.service.impl.ProductServiceImpl;


@Controller
public class MainController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductServiceImpl service;
	private List<Product> products = new ArrayList<>();
	private Product product = new Product();

	@RequestMapping(value = {"/","/main"})
	public String welcomePage() {
		return "l1";
	}
	
	@RequestMapping("/lang")
	public String welcomePageLang() {
		return "l1";
	}
	
	@RequestMapping(value = "/mainPage")
	public String welcomePageLink() {
		return "redirect:/main";
	}
	
	@GetMapping("/about-me")
	public String aboutMe() {
		return "personalPage";
	}
	
	@GetMapping("/contacts")
	public String showContactInfo() {
		return "contactsPage";
	}
	
	@GetMapping("/catalog")
	public String showCatalog(@RequestParam(defaultValue = "")String search_product,Model model) {
		try {
			if (search_product.isEmpty()) {
				products = service.loadAll();
			} else {
				products = service.loadProductByName(search_product);
			}
		} catch (ServiceException e) {
			return "error";
		}
		model.addAttribute("prod",products);
		boolean findProductFlag = products.isEmpty()?true:false;
		model.addAttribute("findProductFlag",findProductFlag);
		return "catalogPage";
	}
	
	
	@GetMapping("/info/{id}")
	public String showPersonalProductPage(@PathVariable String id,Model model) {	
		try {
			product = service.loadFindProductById(Long.parseLong(id));
			model.addAttribute("product", product);
		} catch (ServiceException e) {
			return "error";
		}
		return "personalProductPage";
	}
}

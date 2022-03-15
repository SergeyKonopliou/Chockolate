package com.chockolate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chockolate.model.Product;
import com.chockolate.repository.ProductRepository;


@Controller
public class MainController {
	
	@Autowired
	private ProductRepository productRepository;

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
		return "redirect:/";
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
	public String showCatalog(Model model) {
		Product product = new Product();
		product.setName("apple");
		product.setDescription("sgsdlfgher r hgrel erul thelrh gelghelr ghkewr ge");
		product.setPrice(12.12);
		Product product2 = new Product();
		product2.setName("ball");
		product2.setDescription("rty rturtur yu t yurt uy tyi ty itryityuytuytuyu");
		product2.setPrice(112.1232);
		productRepository.save(product);
		productRepository.save(product2);
		model.addAttribute("prod",product);
		return "catalogPage";
	}
	
}

package com.chockolate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class MainController {

	@RequestMapping(value = {"/","/main"})
	public String welcomePage() {
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
	public String showCatalog() {
		return "catalogPage";
	}
}

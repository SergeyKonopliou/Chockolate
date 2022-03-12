package com.chockolate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class MainController {

	@RequestMapping(value = {"/","/main"})
	public String welcomePage() {
		return "l1";
	}
	
}

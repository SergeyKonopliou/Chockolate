package com.chockolate.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.chockolate.model.User;
import com.chockolate.service.impl.UserDetailsServiceImpl;
/**
 * Класс контроллер,реагирует на адреса связанные с формой регистрации нового пользователя
 *
 */
@Controller
public class RegistrationController {
	@Autowired
	private UserDetailsServiceImpl userService;

	@GetMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("userForm", new User());

		return "registrationPage";
	}

	@PostMapping("/registration")
	public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "registrationPage";
		}
		if (!userService.saveUser(userForm)) {
			model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
			return "registrationPage";
		}

		return "redirect:/login";
	}
}

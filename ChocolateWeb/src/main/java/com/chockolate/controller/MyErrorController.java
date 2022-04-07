package com.chockolate.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chockolate.service.impl.BasketServiceImpl;
import com.chockolate.service.impl.UserDetailsServiceImpl;

/**
 * Контроллер ошибок.Создали класс, реализующий интерфейс ErrorController .
 * Кроме того, нужно установить свойство server.error.path=/error для возврата пользовательского пути
 * при возникновении ошибки при вызове.Таким образом, контроллер может обрабатывать путь вызова / ошибки .В handleError()
 * мы возвращаем созданную нами пользовательскую страницу ошибки в зависимости от типа ошибки:
 * получаем код ошибки и выводим соответствующую страницу.
 *
 */

@Controller
public class MyErrorController implements ErrorController {

	@Autowired
	private BasketServiceImpl basketServiceImpl;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Exception e, Model model) {
		model.addAttribute("userName", userDetailsServiceImpl.getEntryUser());
		model.addAttribute("cart", basketServiceImpl.getProductsInBasket());
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());

			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "error404";
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				model.addAttribute("message", e.getMessage());
				return "error";
			}
		}
		return "error";
	}
}

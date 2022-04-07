package com.chockolate.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.h2.server.web.WebServlet;

/**
 * Класс настройки для входа во встроенную базу данных h2 по адресу localhost:8080/main/console(пароль и логин в properties)
 *
 */

@Configuration
	public class WebConfiguration {
	    @Bean
	    ServletRegistrationBean<WebServlet> h2servletRegistration(){
	        ServletRegistrationBean<WebServlet> registrationBean = new ServletRegistrationBean<WebServlet>( new WebServlet());
	        registrationBean.addUrlMappings("/console/*");
	        return registrationBean;
	    }
	}


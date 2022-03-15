package com.chockolate.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MessageConfig implements WebMvcConfigurer {

	/**
	 * Считывание файлов properties
	 */
	@Bean("messageSource")
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("i18n/rez");
		messageSource.setCacheSeconds(3600);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	/**
	 * Этот бин даёт пользователю возможность самому менять язык системы. Делается
	 * это с помощью передачи дополнительного GET параметра, например, ?lang=en к
	 * любому url адресу приложения. Для этого нужно переопределить бин
	 * LocaleResolver применив реализацию SessionLocaleResolver. Вообще говоря у
	 * LocaleResolver есть несколько реализаций.
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setLocaleAttributeName("session.current.locale");
		slr.setTimeZoneAttributeName("session.current.timezone");
		return slr;
	}

	/**
	 * Теперь нужно реальзовать возможность назначения произвольной локали для
	 * сессии. Для этого определим бин перехватчика LocaleChangeInterceptor. Здесь с
	 * помощью метода setParamName устанавнивается наименование того самого GET
	 * параметра используемого для смены локали(lang).
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}
	/**
	 * Регистрация интерцептора
	 */
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/*");
	}

}

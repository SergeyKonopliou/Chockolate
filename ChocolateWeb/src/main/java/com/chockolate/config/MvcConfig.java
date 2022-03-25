package com.chockolate.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Класс конфигурации для открытия каталога, содержащего загруженные файлы,
 * чтобы клиенты (веб-браузеры) могли получить к ним доступ
 *
 */

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path uploadDir = Paths.get("product-img");
		String uploadPath = uploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("product-img/**").addResourceLocations("file:/" + uploadPath + "/");
		
        registry.addResourceHandler("/css/**")
		        .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
        .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**")
        .addResourceLocations("classpath:/static/images/");

	}

}
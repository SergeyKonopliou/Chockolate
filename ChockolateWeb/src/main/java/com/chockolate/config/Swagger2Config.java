//package com.chockolate.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.hateoas.client.LinkDiscoverer;
//import org.springframework.hateoas.client.LinkDiscoverers;
//import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
//import org.springframework.plugin.core.SimplePluginRegistry;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import static springfox.documentation.builders.PathSelectors.regex;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//@EnableSwagger2
//public class Swagger2Config {
//
//	@Primary
//	@Bean
//	public LinkDiscoverers discoverers() {
//		List<LinkDiscoverer> plugins = new ArrayList<>();
//		plugins.add(new CollectionJsonLinkDiscoverer());
//		return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
//	}
//	
//	/**
//	 * Method to set paths to be included through swagger
//	 *
//	 * @return Docket
//	 */
//	@Bean
//	public Docket configApi() {
//		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).pathMapping("/").select()
//				.paths(regex("/.*")).build();
//	}
//
//
//	/**
//	 * Method to set swagger info
//	 *
//	 * @return ApiInfoBuilder
//	 */
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder().title("Title").description("description").version("1.0").build();
//	}
//}

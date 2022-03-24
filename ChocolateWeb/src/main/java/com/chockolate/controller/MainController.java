package com.chockolate.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Product;
import com.chockolate.model.TypeProduct;
import com.chockolate.service.impl.ProductServiceImpl;

@Controller
public class MainController {

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductServiceImpl service;
	private List<Product> products = new ArrayList<>();
	private Product product = new Product();

	@RequestMapping(value = { "/", "/main" })
	public String welcomePage(Model model) {
		 SecurityContext context = SecurityContextHolder.getContext();
		    Authentication authentication = context.getAuthentication();
		    String userName = authentication.getName();
		model.addAttribute("userName", userName);
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

	@GetMapping(value = { "/login", "/403" })
	public String showLoginForm() {
		return "loginPage";
	}

	@GetMapping("/catalog")
	public String showCatalog(@RequestParam(defaultValue = "") String search_product,
			@RequestParam(defaultValue = "") String select, @RequestParam(defaultValue = "") String selectPrice,
			Model model) {
		try {
			if (!select.isEmpty() && !selectPrice.isEmpty() && !search_product.isEmpty()) {
				products = service.loadAllProductByTypeProductIdAndPriceAndNameContainsIgnoreCase(select, selectPrice,
						search_product);
			} else if (!selectPrice.isEmpty() && !search_product.isEmpty()) {
				products = service.loadAllProductByNameContainsIgnoreCaseAndPrice(search_product, selectPrice);
			} else if (!select.isEmpty() && !selectPrice.isEmpty()) {
				products = service.loadAllProductByTypeProductIdAndPrice(select, selectPrice);
			} else if (!select.isEmpty() && !search_product.isEmpty()) {
				products = service.loadAllProductByTypeProductIdAndProductName(select, search_product);
			} else if (!selectPrice.isEmpty()) {
				products = service.loadAllProductByPrice(selectPrice);
			} else if (!select.isEmpty()) {
				products = service.loadAllProductByTypeProductId(select);
			} else if (!search_product.isEmpty()) {
				products = service.loadProductByName(search_product);
			} else {
				products = service.loadAll();
			}
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		model.addAttribute("prod", products);
		boolean findProductFlag = products.isEmpty() ? true : false;
		model.addAttribute("findProductFlag", findProductFlag);
		List<TypeProduct> list;
		try {
			list = service.loadAllTypeProduct();
			model.addAttribute("types", list);
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}

		return "catalogPage";
	}

	@GetMapping("/info/{id}")
	public String showPersonalProductPage(@PathVariable String id, Model model) {
		try {
			product = service.loadFindProductById(Long.parseLong(id));
			model.addAttribute("product", product);
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "personalProductPage";
	}

	@GetMapping("/addNewProduct")
	public String showAddNewProductPage(Model model) {
		return "addNewProductPage";
	}

	@PostMapping(value = "/add")
	public String addProduct(@PathParam(value = "name") String name, @PathParam(value = "price") String price,
			@PathParam(value = "typeProduct") String typeProduct, @PathParam(value = "description") String description,
			@RequestParam("fileImage") MultipartFile multipartFile, Model model) {
		try {
			product.setName(name);
			product.setPrice(Double.parseDouble(price));
			product.setDescription(description);
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); 
			product.setImage(fileName);
			TypeProduct type = new TypeProduct(typeProduct);
			product.setTypeProduct(type);
			service.add(product, type);
			Product saveProduct = service.loadOneProductByName(name);
			String uploadDir = "product-img/" + saveProduct.getId();
			Path uploadPath = Paths.get(uploadDir);
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			InputStream inputStream = multipartFile.getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (ServiceException | IOException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "redirect:/catalog";
	}

	@GetMapping("/deleteProduct/{id}")
	public String deleteProductById(@PathVariable String id, Model model) {
		try {
			service.delete(Long.parseLong(id));
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "redirect:/catalog";
	}

	@GetMapping("/updateProduct/{id}")
	public String updateProductById(@PathVariable String id, Model model) {
		try {
			product = service.loadFindProductById(Long.parseLong(id));
			model.addAttribute("product", product);
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "updateProductPage";
	}

	@PostMapping(value = "/update")
	public String updateProduct(@PathParam(value = "id") String id, @PathParam(value = "name") String name,
			@PathParam(value = "price") String price, @PathParam(value = "typeProduct") String typeProduct,
			@PathParam(value = "description") String description, @RequestParam("fileImage") MultipartFile multipartFile,
			Model model) {
		try {
			product.setId(Long.parseLong(id));
			product.setName(name);
			product.setPrice(Double.parseDouble(price));
			product.setDescription(description);
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			Product prod = service.loadFindProductById(Long.parseLong(id));
			if(fileName.isEmpty()) {
				product.setImage(prod.getImage());
			}else {
				product.setImage(fileName);
				String uploadDir = "product-img/" + prod.getId();
				Path uploadPath = Paths.get(uploadDir);
				if(!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				InputStream inputStream = multipartFile.getInputStream();
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
			}		
			TypeProduct type = new TypeProduct(typeProduct);
			type.setId(prod.getTypeProduct().getId());
			product.setTypeProduct(type);
			service.update(product, type);
		} catch (ServiceException | IOException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "redirect:/catalog";
	}
}

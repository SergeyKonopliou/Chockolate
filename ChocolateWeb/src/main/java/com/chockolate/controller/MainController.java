package com.chockolate.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.chockolate.service.impl.BasketServiceImpl;
import com.chockolate.service.impl.ProductServiceImpl;
import com.chockolate.service.impl.UserDetailsServiceImpl;

/**
 * Класс отвечает за обработку основных адресов, использующихся вданном
 * приложении
 *
 */
//@Validated
@Controller
public class MainController {

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductServiceImpl service;
	private Page<Product> products;
	private Product product = new Product();
	@Autowired
	private BasketServiceImpl basketServiceImpl;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	/**
	 * Метод реагирует на адреса "/", "/main",возвращает стартовую страницу и узнает
	 * имя вошедшего пользователя
	 */
	@RequestMapping(value = { "/", "/main" })
	public String welcomePage(Model model) {
		model.addAttribute("userName", userDetailsServiceImpl.getEntryUser());
		model.addAttribute("cart", basketServiceImpl.getProductsInBasket());
		return "l1";
	}

	@RequestMapping("/lang")
	public String welcomePageLang() {
		return "redirect:/";
	}

	@RequestMapping(value = "/mainPage")
	public String welcomePageLink() {
		return "redirect:/main";
	}

	@GetMapping("/about-me")
	public String aboutMe(Model model) {
		model.addAttribute("userName", userDetailsServiceImpl.getEntryUser());
		model.addAttribute("cart", basketServiceImpl.getProductsInBasket());
		return "personalPage";
	}

	@GetMapping("/contacts")
	public String showContactInfo(Model model) {
		model.addAttribute("userName", userDetailsServiceImpl.getEntryUser());
		model.addAttribute("cart", basketServiceImpl.getProductsInBasket());
		return "contactsPage";
	}

	@GetMapping(value = { "/login", "/403" })
	public String showLoginForm(Model model) {
		model.addAttribute("userName", userDetailsServiceImpl.getEntryUser());
		model.addAttribute("cart", basketServiceImpl.getProductsInBasket());
		return "loginPage";
	}

	/**
	 * Метод формирует список продуктов из базы данных,выбранных по заданным
	 * критериям.Возвращает страницу каталога.
	 */
	@GetMapping("/catalog")
	public String showCatalog(@RequestParam(defaultValue = "") String search_product,
			@RequestParam(defaultValue = "") String select, @RequestParam(defaultValue = "") String selectPrice,
			Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		model.addAttribute("userName", userDetailsServiceImpl.getEntryUser());
		model.addAttribute("cart", basketServiceImpl.getProductsInBasket());
		model.addAttribute("search_product",search_product);
		model.addAttribute("select",select);
		model.addAttribute("selectPrice",selectPrice);
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);//количество товаров на странице
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		try {
			if (!select.isEmpty() && !selectPrice.isEmpty() && !search_product.isEmpty()) {
				if ("all".equals(select)) {
					products = service.loadAllProductByNameContainsIgnoreCaseAndPrice(pageable,search_product, selectPrice);
				}else {
					products = service.loadAllProductByTypeProductIdAndPriceAndNameContainsIgnoreCase(pageable,select,
							selectPrice, search_product);
				}
			}else if (!selectPrice.isEmpty() && !search_product.isEmpty()) {
				products = service.loadAllProductByNameContainsIgnoreCaseAndPrice(pageable,search_product, selectPrice);
			}else if (!select.isEmpty() && !selectPrice.isEmpty()) {
				if ("all".equals(select)) {
					products = service.loadAllProductByPrice(pageable,selectPrice);
				} else {
					products = service.loadAllProductByTypeProductIdAndPrice(pageable,select, selectPrice);
				}
			}else if (!select.isEmpty() && !search_product.isEmpty()) {
				if ("all".equals(select)) {
					products = service.loadAllProductByName(pageable,search_product);
				} else {
					products = service.loadAllProductByTypeProductIdAndProductName(pageable,select, search_product);
				}
			}else if (!selectPrice.isEmpty()) {
				products = service.loadAllProductByPrice(pageable,selectPrice);
			}else if (!select.isEmpty()) {
				if ("all".equals(select)) {
					products = service.loadAllPaginated(pageable);
				} else {
					products = service.loadAllProductByTypeProductId(pageable,select);
				}
			}else if (!search_product.isEmpty()) {
				products = service.loadAllProductByName(pageable,search_product);
			}else {
				products = service.loadAllPaginated(pageable);
			}
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		int totalPages = products.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("products", products);
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

	/**
	 * Метод возвращает персональную страницу выбранного продукта.
	 */
	@GetMapping("/info/{id}")
	public String showPersonalProductPage(@PathVariable String id, Model model) {
		model.addAttribute("userName", userDetailsServiceImpl.getEntryUser());
		model.addAttribute("cart", basketServiceImpl.getProductsInBasket());
		try {
			product = service.loadProductById(Long.parseLong(id));
			model.addAttribute("product", product);
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "personalProductPage";
	}

	@GetMapping("/addNewProduct")
	public String showAddNewProductPage(Model model) {
		model.addAttribute("userName", userDetailsServiceImpl.getEntryUser());
		model.addAttribute("cart", basketServiceImpl.getProductsInBasket());
		return "addNewProductPage";
	}

	/**
	 * Метод принимает данные для добавления в базу данных.Вызывает метод сервис
	 * слоя для добавления продукта в базу данных
	 */
	@PostMapping(value = "/add")
	public String addProduct(@PathParam(value = "name") String name, @PathParam(value = "price") String price,
			@PathParam(value = "typeProduct") String typeProduct, @PathParam(value = "description") String description,
			@RequestParam("fileImage") MultipartFile multipartFile, Model model) {
		try {
			product.setName(name);
			product.setPrice(Double.valueOf(price));
			product.setDescription(description);
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			product.setImage(fileName);
			TypeProduct type = new TypeProduct(typeProduct);
			product.setTypeProduct(type);
			service.add(product, type);
			Product saveProduct = service.loadOneProductByName(name);
			extracted(multipartFile, fileName, saveProduct);
		} catch (ServiceException | IOException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "redirect:/catalog";
	}

//	 @ExceptionHandler(ConstraintViolationException.class)
//	  @ResponseStatus(HttpStatus.BAD_REQUEST)
//	  public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
//	    return new ResponseEntity<>("not valid date to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//	  }

	private void extracted(MultipartFile multipartFile, String fileName, Product saveProduct) throws IOException {
		String uploadDir = "product-img/" + saveProduct.getId();
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		InputStream inputStream = multipartFile.getInputStream();
		Path filePath = uploadPath.resolve(fileName);
		Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		inputStream.close();
	}

	/**
	 * Метод принимает id продукта,вызывает метод сервис слоя для удаления продукта
	 * по принятому id
	 */
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

	/**
	 * Метод принимает id выбранного для изменения продукта, находит его в базе
	 * данных по id,добавляет найденный продукт в модель и передает на страницу
	 * изменения продукта .
	 */
	@GetMapping("/updateProduct/{id}")
	public String updateProductById(@PathVariable String id, Model model) {
		model.addAttribute("userName", userDetailsServiceImpl.getEntryUser());
		model.addAttribute("cart", basketServiceImpl.getProductsInBasket());
		try {
			product = service.loadProductById(Long.parseLong(id));
			model.addAttribute("product", product);
		} catch (ServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "updateProductPage";
	}

	/**
	 * Метод принимает из формы изменения продукта введенные данные и вызывает метод
	 * сервис слоя для изменения данных в базе данных
	 */
	@PostMapping(value = "/update")
	public String updateProduct(@PathParam(value = "id") String id, @PathParam(value = "name") String name,
			@PathParam(value = "price") String price, @PathParam(value = "typeProduct") String typeProduct,
			@PathParam(value = "description") String description,
			@RequestParam("fileImage") MultipartFile multipartFile, Model model) {
		try {
			product.setId(Long.parseLong(id));
			product.setName(name);
			product.setPrice(Double.valueOf(price));
			product.setDescription(description);
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			Product prod = service.loadProductById(Long.parseLong(id));
			if (fileName.isEmpty()) {
				product.setImage(prod.getImage());
			} else {
				product.setImage(fileName);
				extracted(multipartFile, fileName, prod);
			}
			TypeProduct type = new TypeProduct(typeProduct);
			service.update(product, type);
		} catch (ServiceException | IOException e) {
			model.addAttribute("message", e.getMessage());
			return "error";
		}
		return "redirect:/catalog";
	}
}

package com.chockolate.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Product;
import com.chockolate.model.TypeProduct;
import com.chockolate.repository.ProductRepository;
import com.chockolate.repository.TypeProductRepository;
import com.chockolate.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	@Qualifier("productRepository")
	private ProductRepository repository;
	@Autowired
	@Qualifier("typeProductRepository")
	private TypeProductRepository typeRepository;

	/**
	 * Метод загрузки всех товаров и базы данных
	 */
	public List<Product> loadAll() throws ServiceException {
		List<Product> products = new ArrayList<>();
		try {
			Product product = new Product();
			product.setName("apple");
			product.setDescription("sgsdlfgher r hgrel erul thelrh gelghelr ghkewr ge");
			product.setPrice(12.12);
			product.setImage("images/productImg/apple.jpg");			
			Product product2 = new Product();
			product2.setName("ball");
			product2.setDescription("rty rturtur yu t yurt uy tyi ty itryityuytuytuyu");
			product2.setPrice(112.1232);
			product2.setImage("images/productImg/ball.jpg");
			Product product3 = new Product();
			product3.setName("pen");
			product3.setDescription("ksahgrt rtjtkjhyhelrh gelghelr ghkewr ge");
			product3.setPrice(5.1);
			product3.setImage("images/productImg/pen.jpg");
			
			
			TypeProduct type1 = new TypeProduct("circle");
			TypeProduct type2 = new TypeProduct("line");
			product.setTypeProduct(type1);
			product2.setTypeProduct(type1);
			product3.setTypeProduct(type2);
			type1.getProducts().add(product);
			type1.getProducts().add(product2);
			type2.getProducts().add(product);
			
			typeRepository.save(type1);
			typeRepository.save(type2);
			repository.save(product);
			repository.save(product2);
			repository.save(product3);
			products = repository.findAll();
		} catch (Exception e) {
			throw new ServiceException("Problems with loading all products from DB service method " + e.getMessage(), e);
		}
		return products;
	}

	@Override
	public Product loadFindProductById(Long id) throws ServiceException {
		Product product = new Product();
		try {
			product = repository.findProductById(id);
		} catch (Exception e) {
			throw new ServiceException("Problems with loading all products from DB service method " + e.getMessage(), e);
		}
		return product;
	}

	@Override
	public List<Product> loadProductByName(String name) throws ServiceException {
		List<Product> products = new ArrayList<Product>();
		try {
			products = repository.findProductByName(name);
		} catch (Exception e) {
			throw new ServiceException("Problems with loading searching product from DB service method " + e.getMessage(), e);
		}
		return products;
	}

	@Override
	public void add(Product object,TypeProduct givenTypeProduct) throws ServiceException {
		try {
			Product product = new Product();
			product.setName(object.getName());
			product.setDescription(object.getDescription());
			product.setPrice(object.getPrice());
			product.setImage(object.getImage());
			TypeProduct foundTypeProduct = typeRepository.findTypeProductByName(givenTypeProduct.getName());
			if(foundTypeProduct != null) {
				product.setTypeProduct(foundTypeProduct);
				foundTypeProduct.getProducts().add(product);
				typeRepository.save(foundTypeProduct);
			}else {
				product.setTypeProduct(givenTypeProduct);
				givenTypeProduct.getProducts().add(product);
				typeRepository.save(givenTypeProduct);
			}
			repository.save(product);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new ServiceException("Problems with adding new products to DB service method  " + e.getMessage(), e);
		}
		
	}

	@Override
	public void delete(Long id) throws ServiceException {
		try {
			repository.deleteById(id);
		} catch (Exception e) {
			throw new ServiceException("Problems with deleting product to DB service method " + e.getMessage(), e);
		}
	}

	@Override
	public void update(Product object,TypeProduct givenTypeProduct) throws ServiceException {
		try {
			Product product = new Product();
			product.setId(object.getId());
			product.setName(object.getName());
			product.setDescription(object.getDescription());
			product.setPrice(object.getPrice());
			product.setImage(object.getImage());
			TypeProduct foundTypeProduct = typeRepository.findTypeProductByName(givenTypeProduct.getName());
			if(foundTypeProduct != null) {
				product.setTypeProduct(foundTypeProduct);
				foundTypeProduct.getProducts().add(product);
				typeRepository.save(foundTypeProduct);
			}else {
				product.setTypeProduct(givenTypeProduct);
			}
			repository.save(product);
		} catch (Exception e) {
			throw new ServiceException("Problems with updating product to DB service method  " + e.getMessage(), e);
		}
		
	}

	@Override
	public List<TypeProduct> loadAllTypeProduct() throws ServiceException {
		return typeRepository.findAll();	
	}

	@Override
	public List<Product> loadAllProductByTypeProductId(String typeProduct) throws ServiceException {
		TypeProduct type = typeRepository.findTypeProductByName(typeProduct);
		List<Product> products = repository.findProductByTypeProductId(type.getId());
		return products;
	}

	@Override
	public List<Product> loadAllProductByTypeProductIdAndProductName(String typeName, String productName)
			throws ServiceException {
		TypeProduct type = typeRepository.findTypeProductByName(typeName);
		List<Product> products = repository.findProductByTypeProductIdAndName(type.getId(),productName);
		return products;
	}

}


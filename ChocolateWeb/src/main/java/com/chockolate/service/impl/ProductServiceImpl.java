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
	public List<Product> loadFindProductByName(String name) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(Product object) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Product product) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

}


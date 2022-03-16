package com.chockolate.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Product;
import com.chockolate.repository.ProductRepository;
import com.chockolate.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	@Qualifier("productRepository")
	private ProductRepository repository;

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
			Product product2 = new Product();
			product2.setName("ball");
			product2.setDescription("rty rturtur yu t yurt uy tyi ty itryityuytuytuyu");
			product2.setPrice(112.1232);
			repository.save(product);
			repository.save(product2);
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


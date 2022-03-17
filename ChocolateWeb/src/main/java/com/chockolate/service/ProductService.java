package com.chockolate.service;

import java.util.List;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Product;


public interface ProductService {

	public List<Product> loadAll() throws ServiceException;
	
	public List<Product> loadFindProductByName(String name) throws ServiceException;
	
	public void add(Product object) throws ServiceException;
	
	public void delete(Long id) throws ServiceException;
	
	public void update(Product product) throws ServiceException;

	public Product loadFindProductById(Long id) throws ServiceException;
	
}

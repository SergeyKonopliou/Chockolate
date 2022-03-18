package com.chockolate.service;

import java.util.List;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Product;
import com.chockolate.model.TypeProduct;


public interface ProductService {

	public List<Product> loadAll() throws ServiceException;
	
	public List<Product> loadProductByName(String name) throws ServiceException;
	
	public void add(Product object,TypeProduct typeProduct) throws ServiceException;
	
	public void delete(Long id) throws ServiceException;
	
	public void update(Product product,TypeProduct typeProduct) throws ServiceException;

	public Product loadFindProductById(Long id) throws ServiceException;
	
}

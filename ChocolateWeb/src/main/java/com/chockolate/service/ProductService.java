package com.chockolate.service;

import java.util.List;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Product;
import com.chockolate.model.TypeProduct;

public interface ProductService {

	public void add(Product object, TypeProduct givenTypeProduct) throws ServiceException;

	public void delete(Long id) throws ServiceException;

	public void update(Product product, TypeProduct givenTypeProduct) throws ServiceException;

	public List<Product> loadAll() throws ServiceException;

	public List<Product> loadProductByName(String name) throws ServiceException;
	
	public Product loadOneProductByName(String name) throws ServiceException;

	public Product loadFindProductById(Long id) throws ServiceException;

	public List<TypeProduct> loadAllTypeProduct() throws ServiceException;

	public List<Product> loadAllProductByTypeProductId(String name) throws ServiceException;

	public List<Product> loadAllProductByPrice(String priceSortType) throws ServiceException;

	public List<Product> loadAllProductByTypeProductIdAndProductName(String typeName, String productName)
			throws ServiceException;

	public List<Product> loadAllProductByTypeProductIdAndPrice(String typeName,String priceSortType)
			throws ServiceException;

	public List<Product> loadAllProductByNameContainsIgnoreCaseAndPrice(String name,String priceSortType)
			throws ServiceException;

	public List<Product> loadAllProductByTypeProductIdAndPriceAndNameContainsIgnoreCase(String typeName, String priceSortType,
			String name) throws ServiceException;


}

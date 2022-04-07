package com.chockolate.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Product;
import com.chockolate.model.TypeProduct;

public interface ProductService {

	public void add(Product object, TypeProduct givenTypeProduct) throws ServiceException;

	public void delete(Long id) throws ServiceException;

	public void update(Product product, TypeProduct givenTypeProduct) throws ServiceException;
	
	public Product loadProductById(Long id) throws ServiceException;
	
	public Page<Product> loadAllPaginated(Pageable pageable)throws ServiceException;
	
	public Page<Product> loadAllProductByPrice(Pageable pageable,String priceSortType)throws ServiceException;

	public Page<Product> loadAllProductByName(Pageable pageable,String name) throws ServiceException;

	public Product loadOneProductByName(String name) throws ServiceException;
	
	public List<TypeProduct> loadAllTypeProduct() throws ServiceException;

	public Page<Product> loadAllProductByTypeProductId(Pageable pageable,String name) throws ServiceException;
	

	public Page<Product> loadAllProductByTypeProductIdAndProductName(Pageable pageable,String typeName, String productName)
			throws ServiceException;

	public Page<Product> loadAllProductByTypeProductIdAndPrice(Pageable pageable,String typeName, String priceSortType)
			throws ServiceException;

	public Page<Product> loadAllProductByNameContainsIgnoreCaseAndPrice(Pageable pageable,String name, String priceSortType)
			throws ServiceException;

	public Page<Product> loadAllProductByTypeProductIdAndPriceAndNameContainsIgnoreCase(Pageable pageable,String typeName,
			String priceSortType, String name) throws ServiceException;

}

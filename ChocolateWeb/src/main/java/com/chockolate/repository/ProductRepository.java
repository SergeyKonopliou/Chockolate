package com.chockolate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chockolate.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	public Product findProductById(Long id);

	public List<Product> findProductByName(String name);
	
	public List<Product> findProductByTypeProductId(Long id);
	
	public List<Product> findProductByTypeProductIdAndName(Long id,String name);
}

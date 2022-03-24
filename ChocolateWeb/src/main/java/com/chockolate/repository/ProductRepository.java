package com.chockolate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chockolate.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	public Product findProductById(Long id);

	public Product findProductByName(String name);

	public List<Product> findProductByNameContainsIgnoreCase(String name);

	public List<Product> findProductByTypeProductId(Long id);

	public List<Product> findProductByTypeProductIdAndNameContainsIgnoreCase(Long id, String name);

	public List<Product> findProductByTypeProductIdOrderByPriceAsc(Long id);

	public List<Product> findProductByTypeProductIdOrderByPriceDesc(Long id);

	public List<Product> findProductByNameContainsIgnoreCaseOrderByPriceAsc(String name);

	public List<Product> findProductByNameContainsIgnoreCaseOrderByPriceDesc(String name);

	public List<Product> findProductByTypeProductIdAndNameContainsIgnoreCaseOrderByPriceAsc(Long id, String name);

	public List<Product> findProductByTypeProductIdAndNameContainsIgnoreCaseOrderByPriceDesc(Long id, String name);
}

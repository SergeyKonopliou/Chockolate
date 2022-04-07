package com.chockolate.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chockolate.model.Product;

/**
 * Класс репозиторий для работы с бд и таблицей product и type_product
 * Интерфейсы Page (выведенная страница) и Pagable (для запроса страницы)
 *
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	public Page<Product> findAll(Pageable pageable);

	public Product findProductById(Long id);

	public Product findProductByName(String name);

	public Page<Product> findProductByNameContainsIgnoreCase(String name,Pageable pageable);

	public Page<Product> findProductByTypeProductId(Long id,Pageable pageable);

	public Page<Product> findProductByTypeProductIdAndNameContainsIgnoreCase(Long id, String name,Pageable pageable);

}

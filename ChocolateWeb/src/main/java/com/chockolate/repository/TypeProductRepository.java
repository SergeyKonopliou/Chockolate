package com.chockolate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chockolate.model.TypeProduct;

@Repository
public interface TypeProductRepository extends JpaRepository<TypeProduct, Long> {

	public TypeProduct findTypeProductByName(String name);

}

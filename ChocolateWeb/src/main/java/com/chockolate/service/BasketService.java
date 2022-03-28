package com.chockolate.service;

import java.util.Map;

import com.chockolate.model.Product;


public interface BasketService {


    void addProduct(Product product);

    void removeProduct(Product product);
    
    void clear();

    Map<Product, Integer> getProductsInBasket();

    Double getTotal();
}

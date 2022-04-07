package com.chockolate.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.chockolate.model.Product;
import com.chockolate.service.BasketService;



@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class BasketServiceImpl implements BasketService {

    private Map<Product, Integer> products = new HashMap<>();


	/**
     * If product is in the map just increment quantity by 1.
     * If product is not in the map with, add it with quantity 1
     */
    @Override
    public void addProduct(Product product) {
        if (products.containsKey(product)) {
            products.replace(product, products.get(product) + 1);
        } else {
            products.put(product, 1);
        }
    }

    /**
     * If product is in the map with quantity > 1, just decrement quantity by 1.
     * If product is in the map with quantity 1, remove it from map
     */
    @Override
    public void removeProduct(Product product) {
        if (products.containsKey(product)) {
            if (products.get(product) > 1)
                products.replace(product, products.get(product) - 1);
            else if (products.get(product) == 1) {
                products.remove(product);
            }
        }
    }
    
    /**
     * Clear a shop cart
     */
    @Override
    public void clear() {
      products.clear();
    }

    @Override
    public Map<Product, Integer> getProductsInBasket() {
        return Collections.unmodifiableMap(products);
    }


    @Override
    public Double getTotal() {
        return products.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice()*entry.getValue()).sum();
    }
}

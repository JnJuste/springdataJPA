package com.jnjuste.springdatajpa.service;

import com.jnjuste.springdatajpa.entity.Product;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);
    List<Product> findAllProducts();
    Product findProductById(Long id);
    Product updateProductNameById
            (Long id, String productName)
            throws Exception;
    Product updateProductById(
            Long id, Product updatedProduct)
            throws Exception;

    boolean deleteProductById(Long id);
    List<Product> findProductsAvailableInStock(String active);
}

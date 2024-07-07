package com.jnjuste.springdatajpa.service.impl;

import com.jnjuste.springdatajpa.entity.Product;
import com.jnjuste.springdatajpa.repository.ProductRepository;
import com.jnjuste.springdatajpa.service.ProductService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        } else {
            return product;
        }
    }

    @Override
    public Product updateProductNameById(Long id, String productName) throws Exception {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null){
            product.setProductName(productName);
            return productRepository.save(product);
        } else {
            throw new ObjectNotFoundException(Product.class, String.valueOf(id));
        }
    }

    @Override
    public Product updateProductById(Long id, Product updatedProduct) throws Exception {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Product.class, id.toString()));

        existingProduct.setSku(updatedProduct.getSku());
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setProductDescription(updatedProduct.getProductDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setActive(updatedProduct.isActive());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());

        return productRepository.save(existingProduct);
    }

    @Override
    public boolean deleteProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public List<Product> findProductsAvailableInStock(String active) {
        boolean isActive = Boolean.parseBoolean(active);
        return productRepository.findByActive(isActive);
    }
}

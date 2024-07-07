package com.jnjuste.springdatajpa.controller;

import com.jnjuste.springdatajpa.entity.Product;
import com.jnjuste.springdatajpa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. POST Method (Save new product)
    @PostMapping("/saveProduct")
    public ResponseEntity<Product> saveProductEntity(
            @RequestBody Product product){
        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // 2. GET Method (Read all products we have in DB)
    @GetMapping("/listProducts")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // 3. GET Method (Read Product By ID)
    @GetMapping("/productById/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Product product = productService.findProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 4. PUT Method (To Update Product Information By ID)
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        Product existingProduct = productService.findProductById(id);
        if(existingProduct != null){
            Product updatedProduct = new Product();
            updatedProduct.setProductId(id);
            updatedProduct.setSku(product.getSku());
            updatedProduct.setProductName(product.getProductName());
            updatedProduct.setProductDescription(product.getProductDescription());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setActive(product.isActive());
            updatedProduct.setImageUrl(product.getImageUrl());
            updatedProduct.setLastUpdated(product.getLastUpdated());
            return new ResponseEntity<>(productService.saveProduct(updatedProduct), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 5. DELETE Method (Delete Ticket By ID)
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") Long id) {
        try {
            productService.deleteProductById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

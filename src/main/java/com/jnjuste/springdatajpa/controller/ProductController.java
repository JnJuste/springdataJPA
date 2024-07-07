package com.jnjuste.springdatajpa.controller;

import com.jnjuste.springdatajpa.entity.Product;
import com.jnjuste.springdatajpa.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. POST Method (Inserting New Product)
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
        try {
            Product existingProduct = productService.findProductById(id);
            if (existingProduct == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Update fields of existing product
            existingProduct.setSku(product.getSku());
            existingProduct.setProductName(product.getProductName());
            existingProduct.setProductDescription(product.getProductDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setActive(product.isActive());
            existingProduct.setImageUrl(product.getImageUrl());

            // Save the updated product
            Product updatedProduct = productService.updateProductById(id, existingProduct);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

    // 6. PUT Method (Update a ProductName by ID)
    @PutMapping("/updateProductName/{id}")
    public ResponseEntity<Product> updateProductName(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        try {
            String newProductName = body.get("productName");
            if (newProductName == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Product existingProduct = productService.findProductById(id);
            if (existingProduct == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Update only the product name
            existingProduct.setProductName(newProductName);

            // Save the updated product
            Product updatedProduct = productService.updateProductById(id, existingProduct);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 6. GET Method (To get products which are available in stock(active and inactive or both)
    @GetMapping("/productsInStock")
    public ResponseEntity<List<Product>> getProductsInStock(@RequestParam(required = false) Boolean active) {
        try {
            List<Product> products;
            if (active != null) {
                // If 'active' parameter is provided, filter by active status
                products = productService.findProductsAvailableInStock(active.toString());
            } else {
                // If 'active' parameter is not provided, return all products
                products = productService.findAllProducts();
            }

            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
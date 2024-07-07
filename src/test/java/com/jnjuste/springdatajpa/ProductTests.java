package com.jnjuste.springdatajpa;

import com.jnjuste.springdatajpa.entity.Product;
import com.jnjuste.springdatajpa.repository.ProductRepository;
import com.jnjuste.springdatajpa.service.ProductService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTests {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    // Test for Inserting New Product
    @Test
    @Order(1)
    public void testSaveProduct(){
        // Arrange
        Product prod = new Product();
        prod.setSku("SKU12345");
        prod.setProductName("Test Product");
        prod.setProductDescription("This is a test product description.");
        prod.setPrice(BigDecimal.valueOf(19.99));
        prod.setActive(true);
        prod.setImageUrl("https://example.com/image.jpg");

        Product product = productService.saveProduct(prod);
        assertNotNull(product);
    }

    // Test to find if the number of products are available
    @Test
    @Order(2)
    public void testFindAllProducts(){
        List<Product> products = productService.findAllProducts();
        assertEquals(1, products.size());
    }

    // Test to find a ProductName by ID
    @Test
    @Order(3)
    public void testFindProductNameById(){
        Product product = productService.findProductById(1L);
        assertEquals("Test Product", product.getProductName());
    }

    // Test to Update Product Name by ID
    @Test
    @Order(4)
    public void updateProductNameById() throws Exception {
        Product updateProduct = productService.updateProductNameById(1L, "Test Product Update");
        assertEquals("Test Product Update", updateProduct.getProductName());
    }

    // Unit Test to update an existing product by ID
    @Test
    @Order(5)
    public void testUpdateProductById() throws Exception {
        // Arrange
        Long productId = 2L;
        Product existingProduct = productService.findProductById(productId);
        assertNotNull(existingProduct, "Product with ID " + productId + " should exist");

        Product updatedProduct = new Product();
        updatedProduct.setSku("SKU67890");
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setProductDescription("This is an updated product description.");
        updatedProduct.setPrice(BigDecimal.valueOf(29.99));
        updatedProduct.setActive(false);
        updatedProduct.setImageUrl("https://example.com/updated_image.jpg");

        // Act
        Product resultProduct = productService.updateProductById(productId, updatedProduct);

        // Assert
        assertNotNull(resultProduct, "Updated product should not be null");
        assertEquals(productId, resultProduct.getProductId(), "Product ID should remain unchanged");
        assertEquals(existingProduct.getDateCreated(), resultProduct.getDateCreated(), "Date created should remain unchanged");
        assertEquals("SKU67890", resultProduct.getSku(), "SKU should be updated");
        assertEquals("Updated Product", resultProduct.getProductName(), "Product name should be updated");
        assertEquals("This is an updated product description.", resultProduct.getProductDescription(), "Product description should be updated");
        assertEquals(BigDecimal.valueOf(29.99), resultProduct.getPrice(), "Price should be updated");
        assertFalse(resultProduct.isActive(), "Active status should be updated");
        assertEquals("https://example.com/updated_image.jpg", resultProduct.getImageUrl(), "Image URL should be updated");

        // Verify the update in the database
        Product verifiedProduct = productService.findProductById(productId);
        assertNotNull(verifiedProduct, "Updated product should exist in the database");
        assertEquals(resultProduct.getSku(), verifiedProduct.getSku(), "SKU should match in the database");
        assertEquals(resultProduct.getProductName(), verifiedProduct.getProductName(), "Product name should match in the database");
    }

    // Unit Test for list products available in stock
    @Test
    @Order(6)
    public void testFindProductsAvailableInStock() {
        // Act
        List<Product> activeProducts = productService.findProductsAvailableInStock("true");
        List<Product> inactiveProducts = productService.findProductsAvailableInStock("false");

        // Assert
        assertNotNull(activeProducts, "Active products list should not be null");
        assertNotNull(inactiveProducts, "Inactive products list should not be null");

        System.out.println("Number of active products: " + activeProducts.size());
        System.out.println("Number of inactive products: " + inactiveProducts.size());

        // Check that all products in activeProducts are actually active
        for (Product product : activeProducts) {
            assertTrue(product.isActive(), "All products in activeProducts should be active");
            System.out.println("Active product: " + product.getProductName());
        }

        // Check that all products in inactiveProducts are actually inactive
        for (Product product : inactiveProducts) {
            assertFalse(product.isActive(), "All products in inactiveProducts should be inactive");
            System.out.println("Inactive product: " + product.getProductName());
        }

        // Verify total count
        long totalCount = productRepository.count();
        assertEquals(totalCount, activeProducts.size() + inactiveProducts.size(),
                "Sum of active and inactive products should equal total product count");
    }

    // Unit Test to delete an existing Product
    @Test
    @Order(7)
    public void testDeleteProductById(){
        Boolean isDeleted = productService.deleteProductById(1L);
        Product product = productService.findProductById(1L);

        assertNull(product);
        assertEquals(isDeleted, true);
    }
}

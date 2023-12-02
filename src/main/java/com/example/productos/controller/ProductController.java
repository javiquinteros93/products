package com.example.productos.controller;

import com.example.productos.entity.Product;
import com.example.productos.exceptions.BadRequestException;
import com.example.productos.exceptions.ResourceNotFoundException;
import com.example.productos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseEntity<String> addProduct (@RequestBody Product product) throws BadRequestException {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProductList () {
        return ResponseEntity.ok(productService.productList());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Product> getProductById (@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName (@PathVariable String name) throws ResourceNotFoundException {
        return ResponseEntity.ok(productService.findProductByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editProduct (@PathVariable Long id, @RequestBody Product product) throws ResourceNotFoundException {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct (@PathVariable Long id) throws BadRequestException, ResourceNotFoundException {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/prices")
    public ResponseEntity<List<Product>> getProductListByPrice () {
        return ResponseEntity.ok(productService.sortAllProductsByPrice());
    }

}

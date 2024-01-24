package com.example.productos.service;

import com.example.productos.entity.Product;
import com.example.productos.exceptions.BadRequestException;
import com.example.productos.exceptions.ResourceNotFoundException;
import com.example.productos.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProductService {

    public static final String PRODUCTO_CREADO_EXITOSAMENTE = "Producto creado exitosamente";
    public static final String PRODUCTO_ACTUALIZADO_CORRECTAMENTE = "Producto actualizado correctamente";
    @Autowired
    ProductRepository productRepository;



    public String createProduct (Product product) throws BadRequestException {
        if (product == null) {
            throw new BadRequestException("El producto no cumple los requisitos para ser guardado");
        }

        if (productRepository.findByNameIgnoreCase(product.getName().toLowerCase()).isPresent()) {
            throw new BadRequestException("Ya existe un producto con el nombre: " + product.getName());
        }

        productRepository.save(product);

        return PRODUCTO_CREADO_EXITOSAMENTE;

    }

    public List<Product> productList() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) throws ResourceNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se pudo encontrar el producto"));
    }

    public Product findProductByName(String name) throws ResourceNotFoundException {
        return productRepository.findByNameIgnoreCase(name.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("No se pudo encontrar el producto"));
    }

    public String updateProduct(Long id, Product product) throws ResourceNotFoundException {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    product.setId(id);
                    productRepository.save(product);
                    return PRODUCTO_ACTUALIZADO_CORRECTAMENTE;
                })
                .orElseThrow(() -> new ResourceNotFoundException("ID inválido"));
    }

    public String deleteProduct (Long id) throws BadRequestException, ResourceNotFoundException {
        if (productRepository.findById(id) == null)
            throw new BadRequestException("Id inválido");
        if (productRepository.findById(id).isEmpty())
            throw new ResourceNotFoundException("No se pudo encontrar el producto con ID: " + id);

        productRepository.deleteById(id);
        return "Producto con ID: " + id + " eliminado correctamente";
    }

    public List<Product> sortAllProductsByPrice () {
        return productRepository.findAll()
                .stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .collect(Collectors.toList());
    }



}


package com.example.productos.serviceTest;

import com.example.productos.entity.Product;
import com.example.productos.exceptions.BadRequestException;
import com.example.productos.exceptions.ResourceNotFoundException;
import com.example.productos.repository.ProductRepository;
import com.example.productos.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void createProductSuccessfully() throws BadRequestException {

        Product productToCreate = new Product(1L, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);

        when(productRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        String result = productService.createProduct(productToCreate);

        assertEquals("Producto creado exitosamente", result);

    }

    @Test
    public void createProductWithError() throws BadRequestException {

        Product existingProduct = new Product(1L, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);
        Product productToCreate = new Product(null, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);

        when(productRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(existingProduct));

        try {
            productService.createProduct(productToCreate);
            fail("Se esperaba una BadRequest pero no se lanzó");
        } catch (BadRequestException e) {
            assertEquals("Ya existe un producto con el nombre: " + productToCreate.getName(), e.getMessage());
        }
    }

    @Test
    public void findAllProducts() {

        Product mockProduct = new Product(1L, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);

        when(productRepository.findAll()).thenReturn(Collections.singletonList(mockProduct));

        List<Product> result = productService.productList();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(mockProduct, result.get(0));

    }

    @Test
    public void findProductByIdSuccessfully() throws ResourceNotFoundException {

        Long productId = 1L;

        Product mockProduct = new Product(1L, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        Product result = productService.findProductById(productId);

        assertNotNull(result);
        assertEquals(mockProduct, result);

    }

    @Test
    public void findProductByIdWithError() throws ResourceNotFoundException {

        Long productId = 1L;

        Product mockProduct = new Product(1L, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        try {
            productService.findProductById(productId);
            fail("Se esperaba un ResourceNotFound pero no se lanzó");
        } catch (ResourceNotFoundException e) {
            assertEquals("No se pudo encontrar el producto", e.getMessage());
        }
    }

    @Test
    public void findProductByNameSuccessfully() throws ResourceNotFoundException {

        String productName = "Alfajor";

        Product mockProduct = new Product(1L, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);

        when(productRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(mockProduct));

        Product result = productService.findProductByName(productName);

        assertNotNull(result);
        assertEquals(mockProduct, result);
    }

    @Test
    public void findProductByNameWithError() throws ResourceNotFoundException {

        String productName = "Alfajor";

        Product mockProduct = new Product(1L, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);

        when(productRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        try {
            productService.findProductByName(productName);
            fail("Se esperaba un ResourceNotFound pero no se lanzó");
        } catch (ResourceNotFoundException e) {
            assertEquals("No se pudo encontrar el producto", e.getMessage());
        }
    }

    @Test
    public void updateProductSuccessfully() throws ResourceNotFoundException {

        Long productId = 1L;
        Product existingProduct = new Product(productId, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);
        Product updatedProduct = new Product(productId, "Alfajor", "Dulce de leche y chocolate", 500.0, 500);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        String result = productService.updateProduct(productId, updatedProduct);

        assertEquals("Producto actualizado correctamente", result);

    }

    @Test
    public void updateProductWithInvalidId() {
        Long invalidId = 999L;
        Product updatedProduct = new Product(invalidId, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);

        when(productRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(invalidId, updatedProduct));

    }

    @Test
    public void deleteProductSuccessfully() throws BadRequestException, ResourceNotFoundException {
        Long productId = 1L;

        Product mockProduct = new Product(productId, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        String result = productService.deleteProduct(productId);

        assertEquals("Producto con ID: " + productId + " eliminado correctamente", result);
    }

    @Test
    public void deleteProductWithInvalidId() {
        Long invalidId = 999L;

        when(productRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(invalidId));

    }

    @Test
    public void sortAllProductsByPrice() {
        Product product1 = new Product(1L, "Alfajor", "Dulce de leche y chocolate", 350.0, 500);
        Product product2 = new Product(2L, "Chupetin", "Sabor frutilla", 150.0, 400);
        Product product3 = new Product(3L, "Caramelo", "Masticable, sabor tutti frutti", 75.0, 1500);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2, product3));

        List<Product> sortedProducts = productService.sortAllProductsByPrice();

        assertEquals(3, sortedProducts.size());
        assertTrue(isSortedByPrice(sortedProducts));
    }

    private boolean isSortedByPrice(List<Product> products) {
        for (int i = 0; i < products.size() - 1; i++) {
            if (products.get(i).getPrice() > products.get(i + 1).getPrice()) {
                return false;
            }
        }
        return true;
    }


}

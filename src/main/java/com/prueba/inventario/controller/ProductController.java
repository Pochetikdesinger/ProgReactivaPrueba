package com.prueba.inventario.controller;

import com.prueba.inventario.model.Product;
import com.prueba.inventario.repository.ProductRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getAllProducts() {
        return repository.findAll();
    }

    @PostMapping
    public Mono<Product> createProduct(@RequestBody Product product) {
        System.out.println("Iniciando guardado del producto: " + product.getName());
        Mono<Product> savedProduct = repository.save(product);
        savedProduct.subscribe(
                saved -> System.out.println("Producto guardado con ID: " + saved.getId()),
                error -> System.out.println("Error al guardar el producto: " + error.getMessage())
        );
        return savedProduct;
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
        return repository.findById(id)
                .flatMap(existing -> {
                    existing.setName(product.getName());
                    existing.setPrice(product.getPrice());
                    existing.setStock(product.getStock());
                    return repository.save(existing);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")));
    }
}
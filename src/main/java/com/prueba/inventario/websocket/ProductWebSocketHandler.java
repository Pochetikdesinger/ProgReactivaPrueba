package com.prueba.inventario.websocket;

import com.prueba.inventario.model.Product;
import com.prueba.inventario.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class ProductWebSocketHandler implements WebSocketHandler {

    private final ProductRepository repository;

    public ProductWebSocketHandler(ProductRepository repository) {
        this.repository = repository;
    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(
                repository.findAll()
                        .map(product -> session.textMessage("Product: " + product.getName()))
        );
    }
}
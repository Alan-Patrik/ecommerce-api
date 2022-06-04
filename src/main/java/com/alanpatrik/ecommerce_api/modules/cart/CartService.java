package com.alanpatrik.ecommerce_api.modules.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository repository;

    public Mono<Cart> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Cart with id %s not found", id)))
                ).cast(Cart.class);
    }

    public Mono<Cart> create(Mono<Cart> cart) {
        return cart.flatMap(repository::save).cast(Cart.class);
    }

    public Mono<Cart> update(String id, Mono<Cart> cart) {
        return getById(id).flatMap(p -> cart.doOnNext(e -> e.setId(id))).flatMap(repository::save).cast(Cart.class);

    }

    public Mono<Void> delete(String id) {
        return getById(id).flatMap(p -> repository.deleteById(id));
    }
}

package com.alanpatrik.ecommerce_api.modules.cart;

import com.alanpatrik.ecommerce_api.modules.product.ProductService;
import com.alanpatrik.ecommerce_api.modules.user.User;
import com.alanpatrik.ecommerce_api.modules.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository repository;
    private final UserService userService;

    public Mono<Cart> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Cart with id %s not found", id)))
                ).cast(Cart.class);
    }

    public Mono<Cart> create(Mono<Cart> cart) {
        return cart
                .doOnNext(item -> {
                    Mono<User> user = userService.getById(item.getUserId());
                    user.doOnNext(u -> {
                        item.setUserId(u.getId());
                        log.info("[INFO]: Showing user [ {} ]", u);
                    });
                })
                .flatMap(repository::save)
                .cast(Cart.class);
    }

    public Mono<Cart> update(String id, Mono<Cart> cart) {
        return getById(id)
                .flatMap(p -> cart
                        .doOnNext(e -> e.setId(id)))
                .flatMap(repository::save)
                .cast(Cart.class);

    }

    public Mono<Void> delete(String id) {
        return getById(id).flatMap(p -> repository.deleteById(id));
    }
}

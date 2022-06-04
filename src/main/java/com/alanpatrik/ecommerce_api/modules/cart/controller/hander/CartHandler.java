package com.alanpatrik.ecommerce_api.modules.cart.controller.hander;

import com.alanpatrik.ecommerce_api.modules.cart.Cart;
import com.alanpatrik.ecommerce_api.modules.cart.CartService;
import com.alanpatrik.ecommerce_api.modules.validators.CartValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CartHandler {

    private final CartService service;
    private final Validator validator = new CartValidator();


    public Mono<ServerResponse> getById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getById(id), Cart.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        final Mono<Cart> cartMono = request.bodyToMono(Cart.class).doOnNext(this::validate);
        return ServerResponse
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.create(cartMono), Cart.class);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        final Mono<Cart> cartMono = request.bodyToMono(Cart.class).doOnNext(this::validate);
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.update(id, cartMono), Cart.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ServerResponse
                .status(HttpStatus.NO_CONTENT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.delete(id), Cart.class);
    }

    private void validate(Cart cart) {
        Errors errors = new BeanPropertyBindingResult(cart, "cart");
        validator.validate(cart, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

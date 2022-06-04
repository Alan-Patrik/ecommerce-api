package com.alanpatrik.ecommerce_api.modules.purchase.controller.hander;

import com.alanpatrik.ecommerce_api.modules.purchase.Purchase;
import com.alanpatrik.ecommerce_api.modules.purchase.PurchaseService;
import com.alanpatrik.ecommerce_api.modules.validators.PurchaseValidator;
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
public class PurchaseHandler {

    private final PurchaseService service;
    private final Validator validator = new PurchaseValidator();

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getAll(), Purchase.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        final Mono<Purchase> purchaseMono = request.bodyToMono(Purchase.class).doOnNext(this::validate);
        return ServerResponse
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.create(purchaseMono), Purchase.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ServerResponse
                .status(HttpStatus.NO_CONTENT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.delete(id), Purchase.class);
    }

    private void validate(Purchase purchase) {
        Errors errors = new BeanPropertyBindingResult(purchase, "purchase");
        validator.validate(purchase, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

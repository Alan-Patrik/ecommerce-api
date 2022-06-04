package com.alanpatrik.ecommerce_api.modules.user.controller.handler;

import com.alanpatrik.ecommerce_api.modules.product.Product;
import com.alanpatrik.ecommerce_api.modules.user.User;
import com.alanpatrik.ecommerce_api.modules.user.UserService;
import com.alanpatrik.ecommerce_api.modules.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserService service;
    private final Validator validator = new UserValidator();

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(service.getAll(), User.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        String id = request.pathVariable("id");

        return ServerResponse
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(service.getById(id), User.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class).doOnNext(this::validate);

        return ServerResponse
                .status(CREATED)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(userMono.flatMap(service::create), User.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<User> userMono = request.bodyToMono(User.class).doOnNext(this::validate);

        return ServerResponse
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(userMono.flatMap(p -> service.update(id, p)), User.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");

        return ServerResponse
                .status(NO_CONTENT)
                .contentType(APPLICATION_JSON)
                .body(service.delete(id), User.class);
    }

    private void validate(User user) {
        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

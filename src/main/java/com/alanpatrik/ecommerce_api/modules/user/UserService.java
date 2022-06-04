package com.alanpatrik.ecommerce_api.modules.user;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserService {

    private final WebClient webClient;
    private Flux<User> CACHE;

    public UserService(WebClient.Builder builder) {
        webClient = builder.baseUrl("http://localhost:8082/api/v1").build();
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "getAllUsersCache")
    @Retry(name = "user-service", fallbackMethod = "getAllUsersCache")
    public Flux<User> getAll() {
        log.info("[INFO]: Returning all users");
        return webClient
                .get()
                .uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Error returning all users")))
                .bodyToFlux(User.class)
                .flatMap(users -> {
                    Flux<User> usersFlux = Flux.just(users);
                    log.info("[INFO]: Adding users to the cache [ {} ]", users);
                    CACHE = usersFlux.flatMap(Flux::just);
                    return usersFlux;
                });
    }

    public Mono<User> getById(String id) {
        log.info("[INFO]: Returning users with id [ {} ]", id);
        return webClient
                .get()
                .uri("/users/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("User with id %s not found", id))))
                .bodyToMono(User.class);
    }

    public Mono<User> create(User user) {
        log.info("[INFO]: Creating users [ {} ]", user);
        return webClient
                .post()
                .uri("/users")
                .bodyValue(user)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                String.format("User with username %s has already been registered", user.getUsername()))))
                .bodyToMono(User.class);
    }

    public Mono<User> update(String id, User user) {
        log.info("[INFO]: Updating users [ {} ]", user);
        return webClient
                .put()
                .uri("/users/" + id)
                .bodyValue(user)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("User with id %s not found", id))))
                .bodyToMono(User.class);
    }

    public Mono<User> delete(String id) {
        log.info("[INFO]: Deleting users with the id [ {} ]", id);
        return webClient
                .delete()
                .uri("/users/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("User with id %s not found", id))))
                .bodyToMono(User.class);
    }

    private Flux<User> getAllUsersCache(Throwable e) {
        log.info("[INFO]: Returning all users from cache");
        return CACHE;
    }

}

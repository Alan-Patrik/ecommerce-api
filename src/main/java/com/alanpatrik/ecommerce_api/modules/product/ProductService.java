package com.alanpatrik.ecommerce_api.modules.product;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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
public class ProductService {

    private final WebClient webClient;
    private Flux<Product> CACHE;

    public ProductService(WebClient.Builder builder) {
        webClient = builder.baseUrl("http://localhost:8081/api/v1").build();
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "getAllProductsCache")
    @Retry(name = "product-service", fallbackMethod = "getAllProductsCache")
    public Flux<Product> getAll() {
        log.info("[INFO]: Returning all products");
        return webClient
                .get()
                .uri("/products")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Error returning all products")))
                .bodyToFlux(Product.class)
                .flatMap(products -> {
                    Flux<Product> productFlux = Flux.just(products);
                    log.info("[INFO]: Adding products to the cache [ {} ]", products);
                    CACHE = productFlux.flatMap(Flux::just);
                    return productFlux;
                });
    }

    public Mono<Product> getById(String id) {
        log.info("[INFO]: Returning product with id [ {} ]", id);
        return webClient
                .get()
                .uri("/products/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Product with id %s not found", id))))
                .bodyToMono(Product.class);
    }

    public Mono<Product> create(Product product) {
        log.info("[INFO]: Creating product [ {} ]", product);
        return webClient
                .post()
                .uri("/products")
                .bodyValue(product)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                String.format("Product with name %s has already been registered", product.getName()))))
                .bodyToMono(Product.class);
    }

    public Mono<Product> update(String id, Product product) {
        log.info("[INFO]: Updating product [ {} ]", product);
        return webClient
                .put()
                .uri("/products/" + id)
                .bodyValue(product)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Product with id %s not found", id))))
                .bodyToMono(Product.class);
    }

    public Mono<Product> updateStorage(String id, Product product) {
        log.info("[INFO]: Updating product stock [ {} ]", product);
        return webClient
                .patch()
                .uri("/products/update/storage/" + id)
                .bodyValue(product)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Product with id %s not found", id))))
                .bodyToMono(Product.class);
    }

    public Mono<Product> delete(String id) {
        log.info("[INFO]: Deleting product with the id [ {} ]", id);
        return webClient
                .delete()
                .uri("/products/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Product with id %s not found", id))))
                .bodyToMono(Product.class);
    }

    private Flux<Product> getAllProductsCache(Throwable e) {
        log.info("[INFO]: Returning all products from cache");
        return CACHE;
    }
}
package com.alanpatrik.ecommerce_api.modules.purchase;

import com.alanpatrik.ecommerce_api.modules.cart.Cart;
import com.alanpatrik.ecommerce_api.modules.cart.CartRepository;
import com.alanpatrik.ecommerce_api.modules.product.Product;
import com.alanpatrik.ecommerce_api.modules.product.ProductService;
import com.alanpatrik.ecommerce_api.modules.product.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository repository;
    private final ProductService productService;
    private final CartRepository cartRepository;

    public Flux<Purchase> getAll() {
        return repository.findAll();
    }

    public Mono<Purchase> create(Mono<Purchase> purchase) {
        return purchase
                .doOnNext(item -> {
                    item.getCart().getProducts().stream()
                            .map(product -> productService.updateStorage(product.getId(), product))
                            .collect(Collectors.toList());

                    item.setTotal(totalPrice(item.getCart().getProducts()));
                    item.setItemsCart(item.getCart().getProducts().size());
                })
                .flatMap(repository::save)
                .doOnNext(c -> log.info("[INFO]: Deleting cart [ {} ]", c.getCart()))
                .doOnNext(item -> cartRepository.save(new Cart()))
                .cast(Purchase.class);
    }

    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Purchase with id %s not found", id)))
                )
                .doOnNext(p -> log.info("[INFO]: Deleting purchase with id [ {} ]", id))
                .flatMap(p -> repository.deleteById(id));

    }

    private double totalPrice(List<Product> products) {
        double sum = 0;
        double aux = 0;
        int qty = 0;

        for (Product product : products) {
            qty += product.getQty();
            aux += qty * product.getPrice();
            sum += aux;

            qty = 0;
            aux = 0;
        }

        return sum;
    }
}

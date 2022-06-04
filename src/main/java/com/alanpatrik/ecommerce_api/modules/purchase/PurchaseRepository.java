package com.alanpatrik.ecommerce_api.modules.purchase;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends ReactiveCrudRepository<Purchase, String> {
}

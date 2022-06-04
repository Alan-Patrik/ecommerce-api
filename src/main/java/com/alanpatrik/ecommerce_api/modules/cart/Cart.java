package com.alanpatrik.ecommerce_api.modules.cart;

import com.alanpatrik.ecommerce_api.modules.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cart")
public class Cart {

    @Id
    private String id;
    private List<Product> products = new ArrayList<>();
}

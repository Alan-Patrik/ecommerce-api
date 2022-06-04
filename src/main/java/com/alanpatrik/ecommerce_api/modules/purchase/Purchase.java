package com.alanpatrik.ecommerce_api.modules.purchase;

import com.alanpatrik.ecommerce_api.modules.cart.Cart;
import com.alanpatrik.ecommerce_api.modules.user.dto.UserPurchaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "purchase")
public class Purchase {

    @Id
    private String id;
    private UserPurchaseDTO user;
    private Cart cart;
    private double total;
    private int itemsCart;
}

package com.alanpatrik.ecommerce_api.modules.validators;

import com.alanpatrik.ecommerce_api.modules.cart.Cart;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

public class CartValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Cart.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Cart cart = (Cart) target;

        if (cart.getProducts().size() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'PRODUCTS' field is required!");
        }
    }
}

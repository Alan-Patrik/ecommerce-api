package com.alanpatrik.ecommerce_api.modules.validators;

import com.alanpatrik.ecommerce_api.modules.purchase.Purchase;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

public class PurchaseValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Purchase.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Purchase purchase = (Purchase) target;

        if (purchase.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'USER-ID' field is required!");
        }

        if (purchase.getCart().getProducts().size() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'PRODUCTS' field is required!");
        }
    }
}

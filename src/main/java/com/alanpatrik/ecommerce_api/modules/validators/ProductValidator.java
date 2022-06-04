package com.alanpatrik.ecommerce_api.modules.validators;

import com.alanpatrik.ecommerce_api.modules.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class ProductValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return Product.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        Product p = (Product) obj;

        if (p.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'NAME' field is required!");
        }

        if (p.getPrice() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("'PRICE' field cannot be 0.0", p.getPrice()));
        }
    }
}

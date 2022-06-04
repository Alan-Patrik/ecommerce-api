package com.alanpatrik.ecommerce_api.modules.validators;

import com.alanpatrik.ecommerce_api.modules.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

public class UserValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        User p = (User) obj;

        if (p.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'NAME' field is required!");
        }

        if (p.getUsername() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'USERNAME' field is required!");
        }

        if (p.getRole() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'ROLE' field is required!");
        }
    }
}

package com.techelevator.validator;

public interface Validator {
    boolean supports(Class<?> aClass);

    void validate(Object o, Errors errors);
}

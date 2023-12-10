package com.techelevator.validator;

public interface Validator<T> {
    boolean supports(Class<?> aClass);

    void validate(T t, ErrorResult errorResult);
}

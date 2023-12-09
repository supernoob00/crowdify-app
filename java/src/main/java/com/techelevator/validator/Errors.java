package com.techelevator.validator;

import java.util.List;

public class Errors {
    private List<String> causes;

    public void reject(String cause) {
        causes.add(cause);
    }

    public void rejectValue(String field, String cause) {
        causes.add(cause);
    }

    public List<String> getCauses() {
        return causes;
    }
}

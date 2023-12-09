package com.techelevator.validator;

import java.util.ArrayList;
import java.util.List;

public class ErrorResult {
    private List<String> causes = new ArrayList<>();

    public void reject(String cause) {
        causes.add(cause);
    }

    public void rejectValue(String field, String cause) {
        causes.add(cause);
    }

    public List<String> getCauses() {
        return causes;
    }

    public boolean hasErrors() {
        return !causes.isEmpty();
    }
}

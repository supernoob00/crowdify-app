package com.techelevator.validator;

import com.techelevator.model.Donation;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DonationValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Donation.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Donation donation = (Donation) o;

        // validate anonymous if user is null
        if (donation.getDonor() == null && !donation.isAnonymous()) {
            errors.reject("Donation must be marked anonymous if user is null");
        }

        // TODO: validate valid campaign id ?
        // TODO: validate campaign is not deleted
    }
}

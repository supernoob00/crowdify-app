package com.techelevator.validator;

import com.techelevator.model.NewSpendRequestDto;

import java.time.LocalDateTime;

public class NewSpendRequestDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return NewSpendRequestDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, ErrorResult errorResult) {
        NewSpendRequestDto dto = (NewSpendRequestDto) o;

        if (dto.getEndDate().isBefore(LocalDateTime.now())) {
            errorResult.rejectValue("d", "d");
        }
    }

    // TODO: validate end date

    // TODO: validate campaign id

    // TODO: validate campaign is not locked or deleted

    // TODO: validate campaign is in second phase

    // TODO: validate amount less than or equal to current funds
}

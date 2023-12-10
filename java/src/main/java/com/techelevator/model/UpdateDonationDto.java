package com.techelevator.model;

import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UpdateDonationDto {
    @Min(1) @Max(50_000_000)
    private int amount;
    @NotBlank
    private String comment;

    public UpdateDonationDto(int amount, String comment) {
        this.amount = amount;
        this.comment = comment;
    }

    public UpdateDonationDto() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

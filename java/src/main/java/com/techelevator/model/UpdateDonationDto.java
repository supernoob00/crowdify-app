package com.techelevator.model;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UpdateDonationDto {
    @Min(1) @Max(50_000_000)
    private int amount;
    @Length(max = 10000)
    private String comment;
    private boolean refunded;

    public UpdateDonationDto(int id, int amount, String comment, boolean refunded) {
        this.amount = amount;
        this.comment = comment;
        this.refunded = refunded;
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

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }
}

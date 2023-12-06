package com.techelevator.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class NewSpendRequestDto {

    @Min(1)
    private int id;
    @Min(1)
    private int campaign_id;
    @Positive
    private int amount;
    @NotBlank
    private String description;
    @NotNull
    private boolean approved;
    @NotNull
    private LocalDateTime endDate;

}

package com.freelancing.freelancing_platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProposalRequest {

    @NotNull(message = "Bid amount is required")
    @Positive(message = "Bid amount must be greater than zero")
    private Double bidAmount;

    @NotBlank(message = "Cover letter is required")
    private String coverLetter;

    @NotNull(message = "Delivery time is required")
    @Positive(message = "Delivery time must be greater than zero")
    private Integer deliveryTime;
}
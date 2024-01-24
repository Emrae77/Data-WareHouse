package com.ehimare.progressSoft.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
public class DealDTO {
    private Long id;
    @NotNull(message = "currency field cannot be null")
    private Currency orderingCurrency;
    @NotNull(message = "currency field cannot be null")
    private Currency quoteCurrency;
    @NotNull
    private LocalDateTime dealTime = LocalDateTime.now();
    @NotNull(message = "amount field cannot be null")
    private BigDecimal dealAmount;

}

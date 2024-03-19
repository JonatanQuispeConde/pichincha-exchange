package com.pichincha.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LogExchangeDto {

    @Positive
    @NotNull
    private int idExchangeMoney;
    private double valueExchangeMoney;
    private String exchangeMoneyName;

    @Positive
    @NotNull
    private int idExchangeQuote;
    private double valueExchangeQuote;
    private String exchangeQuoteName;

    @Min(value = 0, message = "el monto debe ser mayor que cero")
    private double amount;

    private double amountChanged;
    private String userChanged;
}

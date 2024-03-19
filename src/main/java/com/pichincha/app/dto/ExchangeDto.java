package com.pichincha.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExchangeDto {


    @NotBlank(message = "tipo moneda es requerido")
    private String typeCurrency;
    @Min(value = 1, message = "la unidad valor debe ser mayor que cero")
    private double unit;
    @Min(value = 0, message = "el valor en soles debe ser mayor que cero soles")
    private double valueSoles;

}

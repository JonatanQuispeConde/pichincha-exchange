package com.pichincha.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table(name = "exchange")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Exchange {

    @Id
    private int id;
    private String typeCurrency;
    private double unit;
    private double valueSoles;

}

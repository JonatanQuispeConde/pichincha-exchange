package com.pichincha.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "log_exchange")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LogExchange {

    @Id
    private int id;
    private int idExchangeMoney;
    private double valueExchangeMoney;
    private int idExchangeQuote;
    private double valueExchangeQuote;
    private double unit;
    private double totalValue;
    private String userChanged;
}


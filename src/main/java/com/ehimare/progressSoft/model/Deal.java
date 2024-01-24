package com.ehimare.progressSoft.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
@Entity
@Table(name = "Deal")
@NoArgsConstructor
@AllArgsConstructor
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    @Column(name = "from_currency", nullable = false)
    private Currency orderingCurrency;
    @Column(name = "to_currency", nullable = false)
    private Currency quoteCurrency;
    @Column(name = "time", nullable = false)
    private LocalDateTime dealTime;
    @Column(name = "amount", nullable = false)
    private BigDecimal dealAmount;

    public Deal(Currency orderingCurrency, Currency quoteCurrency, LocalDateTime dealTime, BigDecimal dealAmount) {
        this.orderingCurrency = orderingCurrency;
        this.quoteCurrency = quoteCurrency;
        this.dealTime = dealTime;
        this.dealAmount = dealAmount;
    }
}

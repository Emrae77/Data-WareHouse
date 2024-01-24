package com.ehimare.progressSoft;

import com.ehimare.progressSoft.dto.DealDTO;
import com.ehimare.progressSoft.model.Deal;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public class TestFixtures {
    private static final ModelMapper mapper = new ModelMapper();
    public static DealDTO createDealDTOWithCurrencyAndAmount(final String orderingCurrency,
                                                       final String quoteCurrency, final Double amount) {
        DealDTO dealDTO = new DealDTO();
        dealDTO.setOrderingCurrency(Currency.getInstance(orderingCurrency));
        dealDTO.setQuoteCurrency(Currency.getInstance(quoteCurrency));
        dealDTO.setDealTime(LocalDateTime.now());
        dealDTO.setDealAmount(BigDecimal.valueOf(amount));

        return dealDTO;
    }

    public static Deal createDealWithCurrencyAndAmount(final String orderingCurrency,
                                                 final String quoteCurrency, final Double amount) {
        return mapper.map(createDealDTOWithCurrencyAndAmount(orderingCurrency, quoteCurrency, amount), Deal.class);
    }
}

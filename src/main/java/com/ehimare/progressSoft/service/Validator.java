package com.ehimare.progressSoft.service;

import com.ehimare.progressSoft.dto.DealDTO;
import com.ehimare.progressSoft.exception.ValidatorException;

public class Validator {
    public static void validate(final DealDTO dealDTO) {
        if (dealDTO.getOrderingCurrency().equals(dealDTO.getQuoteCurrency())) {
            throw new ValidatorException("invalid request, ordering and quote currency cannot be the same");
        }
    }
}

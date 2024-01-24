package com.ehimare.progressSoft.controller;

import com.ehimare.progressSoft.TestFixtures;
import com.ehimare.progressSoft.dto.DealDTO;
import com.ehimare.progressSoft.model.Deal;
import com.ehimare.progressSoft.repository.DealRepository;
import com.ehimare.progressSoft.service.DealService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles = "test")
class DealControllerTest {
    @Autowired
    DealService dealService;
    @Autowired
    DealRepository dealRepository;


    @Autowired
    private MockMvc mockMvc;

    private static final String ERROR_MESSAGE_SAME_CURRENCY = "invalid request, ordering and quote currency cannot be the same";
    private static final String ERROR_MESSAGE_ID_NOT_FOUND = "deal with id 1 not found";

    private static final ObjectMapper mapper = createObjectMapper();

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }


    @AfterEach
    void setUp() {
        dealRepository.deleteAll();
    }

    @Test
    void createFxDealSuccessFully() throws Exception {
        final DealDTO dealDTO = TestFixtures.createDealDTOWithCurrencyAndAmount("USD", "GBP", 20.0);
//        when(dealService.createDeal(dealDTO)).thenReturn(dealDTO);

        mockMvc
                .perform(post("/deals").contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(dealDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dealAmount").value(dealDTO.getDealAmount()))
                .andExpect(jsonPath("$.orderingCurrency").value(dealDTO.getOrderingCurrency().getCurrencyCode()))
                .andExpect(jsonPath("$.quoteCurrency").value(dealDTO.getQuoteCurrency().getCurrencyCode()));


        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize( 1);
        final Deal savedDeal = dealList.get(0);
        assertThat(savedDeal.getDealAmount().doubleValue()).isEqualTo(dealDTO.getDealAmount().doubleValue());
        assertThat(savedDeal.getOrderingCurrency()).isEqualTo(dealDTO.getOrderingCurrency());
        assertThat(savedDeal.getQuoteCurrency()).isEqualTo(dealDTO.getQuoteCurrency());
        assertThat(savedDeal.getDealTime()).isEqualTo(dealDTO.getDealTime());
    }

    @Test
    void createDealFailsWithBadRequest() throws Exception {
        final DealDTO dealDTO = TestFixtures.createDealDTOWithCurrencyAndAmount("GBP", "GBP", 20.0);

        mockMvc
                .perform(post("/deals").contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(dealDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(ERROR_MESSAGE_SAME_CURRENCY))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.name()));
    }


    @Test
    void getAllDealsSuccessfully() throws Exception {
        dealRepository.save(TestFixtures.createDealWithCurrencyAndAmount("USD", "NGN", 100.0));
        mockMvc
                .perform(get("/deals/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].dealAmount").value(100.0))
                .andExpect(jsonPath("$[0].orderingCurrency").value("USD"))
                .andExpect(jsonPath("$[0].quoteCurrency").value("NGN"));
    }

    @Test
    void getAllDealsSuccessfullyWhenArrayIsEmpty() throws Exception {
        mockMvc
                .perform(get("/deals/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].dealAmount").doesNotExist())
                .andExpect(jsonPath("$[0].orderingCurrency").doesNotExist())
                .andExpect(jsonPath("$[0].quoteCurrency").doesNotExist());
    }

    @Test
    void getAllDealByIDSuccessfully() throws Exception {
        final DealDTO dealDTO = TestFixtures.createDealDTOWithCurrencyAndAmount("USD", "EUR", 20.0);
        final Deal deal = TestFixtures.createDealWithCurrencyAndAmount("USD", "EUR", 20.0);

        final Deal savedDeal = dealRepository.save(deal);
        mockMvc
                .perform(get("/deals/" + savedDeal.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.dealAmount").value(dealDTO.getDealAmount()))
                .andExpect(jsonPath("$.orderingCurrency").value(dealDTO.getOrderingCurrency().getCurrencyCode()))
                .andExpect(jsonPath("$.quoteCurrency").value(dealDTO.getQuoteCurrency().getCurrencyCode()));
    }

    @Test
    void createDealFailsWithNotFound() throws Exception {
        mockMvc
                .perform(get("/deals/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value(ERROR_MESSAGE_ID_NOT_FOUND))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.name()));
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}

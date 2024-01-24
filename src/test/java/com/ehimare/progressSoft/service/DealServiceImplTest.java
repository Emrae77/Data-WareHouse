package com.ehimare.progressSoft.service;

import com.ehimare.progressSoft.dto.DealDTO;
import com.ehimare.progressSoft.exception.DealNotSavedException;
import com.ehimare.progressSoft.exception.ResourceNotFound;
import com.ehimare.progressSoft.model.Deal;
import com.ehimare.progressSoft.repository.DealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ehimare.progressSoft.TestFixtures.createDealDTOWithCurrencyAndAmount;
import static com.ehimare.progressSoft.TestFixtures.createDealWithCurrencyAndAmount;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class DealServiceImplTest {
    @Mock
    private DealRepository dealRepository;

    private ModelMapper mapper;

    private DealServiceImpl dealService;

    @BeforeEach
    void setUp() {
        dealRepository = mock(DealRepository.class);
        mapper = new ModelMapper();
        dealService = new DealServiceImpl(dealRepository, mapper);
    }

    @Test
    void createDealSuccessfully() throws DealNotSavedException {
        // given
        final DealDTO dealDTO = createDealDTOWithCurrencyAndAmount("USD", "EUR", 100.0);
        final Deal deal = mapper.map(dealDTO, Deal.class);
        when(dealRepository.save(Mockito.any(Deal.class))).thenReturn(deal);

        // when
        DealDTO createdDealDTO = dealService.createDeal(dealDTO);

        // then
        assertEquals(dealDTO.getDealAmount(), createdDealDTO.getDealAmount());
        assertEquals(dealDTO.getDealTime(), createdDealDTO.getDealTime());
    }





    @Test
    void getAllDealsSuccessfully() {
        // given
        final Deal firstDeal = createDealWithCurrencyAndAmount("USD", "EUR", 100.0);
        final Deal secondDeal = createDealWithCurrencyAndAmount("USD", "NGN", 50.24);
        final List<Deal> dealList = List.of(firstDeal, secondDeal);
        when(dealRepository.findAll()).thenReturn(dealList);

        // when
        final List<DealDTO> resultDealsDTO = dealService.getAllDeals();

        // then
        assertEquals(resultDealsDTO.size(), dealList.size());
        assertNotNull(resultDealsDTO.get(0));
        assertEquals(resultDealsDTO.get(0).getDealAmount(), dealList.get(0).getDealAmount());
        assertEquals(resultDealsDTO.get(0).getDealTime(), dealList.get(0).getDealTime());
        assertEquals(resultDealsDTO.get(0).getOrderingCurrency(), dealList.get(0).getOrderingCurrency());
        assertEquals(resultDealsDTO.get(0).getQuoteCurrency(), dealList.get(0).getQuoteCurrency());
    }

    @Test
    void getAllDealsSuccessfullyWhenListIsEmpty() {
        // given
        when(dealRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        final List<DealDTO> resultDealsDTO = dealService.getAllDeals();

        // then
        assertNotNull(resultDealsDTO);
        assertEquals(resultDealsDTO.size(), 0);

    }

    @Test
    void getDealByIdSuccessfully() {
        // given: mock Deal object
        final Deal mockDeal = createDealWithCurrencyAndAmount("USD","CNY",250.4);
        when(dealRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(mockDeal));

        // then: verify the result
        final DealDTO fetchedDeal =  dealService.getDealById(1l);

        assertEquals(fetchedDeal.getId(), mockDeal.getId());
        assertEquals(fetchedDeal.getDealAmount(), mockDeal.getDealAmount());
        assertEquals(fetchedDeal.getOrderingCurrency(), mockDeal.getOrderingCurrency());
        assertEquals(fetchedDeal.getQuoteCurrency(), mockDeal.getQuoteCurrency());
    }

    @Test
    void  getDealByIdWhenIdIsInvalid(){
        when(dealRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFound.class, ()->  dealService.getDealById(1L));
    }

    @Test
    void deleteByIdSuccessfully(){
        final Deal mockDeal = createDealWithCurrencyAndAmount("USD","NGN",400.2);
        when(dealRepository.findById(1l)).thenReturn(Optional.of(mockDeal));

        dealService.deleteDeal(1l);

        verify(dealRepository, times(1)).deleteById(anyLong());
    }
    @Test
    void deleteIdWhenIdIsInvalid(){
        when(dealRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFound.class, ()-> dealService.deleteDeal(1l));
    }
}

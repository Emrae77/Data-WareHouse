package com.ehimare.progressSoft.service;

import com.ehimare.progressSoft.dto.DealDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface DealService {
    public DealDTO createDeal (DealDTO dealDTO);
    public List<DealDTO> getAllDeals();
    public DealDTO getDealById(Long id);
    public void deleteDeal(Long id);

}




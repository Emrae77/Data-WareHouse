package com.ehimare.progressSoft.service;

import com.ehimare.progressSoft.dto.DealDTO;
import com.ehimare.progressSoft.exception.ResourceNotFound;
import com.ehimare.progressSoft.model.Deal;
import com.ehimare.progressSoft.repository.DealRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class DealServiceImpl implements DealService {
    private DealRepository dealRepository;

    private ModelMapper mapper;


    @Override
    public DealDTO createDeal(final DealDTO dealDTO) {
        Validator.validate(dealDTO);
        final Deal saveDeal = dealRepository.save(mapper.map(dealDTO, Deal.class));
        log.info("saved deal to database: {}", dealDTO);
        return mapper.map(saveDeal, DealDTO.class);
    }

    @Override
    public List<DealDTO> getAllDeals() {
        List<Deal> deals = dealRepository.findAll();
        log.info("successfully retrieved all deals ");
        return deals.stream().map(deal -> mapper.map(deal, DealDTO.class)).collect(Collectors.toList());
    }

    @Override
    public DealDTO getDealById(final Long id) {
        Optional<DealDTO> optionalDeal = dealRepository.findById(id).map(deal -> mapper.map(deal, DealDTO.class));
        log.info("fetched deal by id {}", id);
        return optionalDeal.orElseThrow(()-> new ResourceNotFound("deal with id " + id + " not found"));
    }



    @Override
    public void deleteDeal(final Long id)throws ResourceNotFound {
        log.info("deleting deal by id: {}", id);
        Optional<Deal> optionalDeal = dealRepository.findById(id);
        optionalDeal.ifPresentOrElse(
                (deal) -> dealRepository.deleteById(id),
                () -> {throw new ResourceNotFound("deal with id " + id + " not found"); });
    }

}

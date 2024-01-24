package com.ehimare.progressSoft.controller;


import com.ehimare.progressSoft.dto.DealDTO;
import com.ehimare.progressSoft.service.DealService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/deals")
@AllArgsConstructor
public class DealController {
    private DealService dealService;

    @PostMapping({"", "/"})
    public ResponseEntity<DealDTO> createDeal(final @Valid @RequestBody DealDTO dealDTO) throws URISyntaxException {
        log.info("saved deal using http request: {}", dealDTO);
        final DealDTO createdDealDTO = dealService.createDeal(dealDTO);
        log.info("deal saved successfully: {}", dealDTO);
        return ResponseEntity.created(new URI(String.format("%s%s", "/deals/", createdDealDTO.getId())))
               .body(createdDealDTO);
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<DealDTO>> getAllDeals() {
        log.debug("request to get all deals");
        return ResponseEntity.ok().body(dealService.getAllDeals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DealDTO> getDealById(final @PathVariable Long  id) {
        log.info("retrieving deal with id {}: ", id);
        return  ResponseEntity.ok().body(dealService.getDealById(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeal(final @PathVariable Long  id) {
        log.info("deleting deal with id {}", id);
        dealService.deleteDeal(id);
        log.info("deal deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }
}




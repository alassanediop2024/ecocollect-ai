package com.ecocollect.controller;

import com.ecocollect.dto.SectorRequest;
import com.ecocollect.dto.SectorResponse;
import com.ecocollect.service.SectorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sectors")
public class SectorController {

    private final SectorService sectorService;

    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @GetMapping
    public List<SectorResponse> getAll(
            @RequestParam(required = false) Long municipalityId) {

        if (municipalityId != null) {
            return sectorService.findByMunicipality(municipalityId);
        }

        return sectorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectorResponse> getById(@PathVariable Long id) {
        return sectorService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SectorResponse> create(
            @Valid @RequestBody SectorRequest request) {

        SectorResponse created = sectorService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }
}

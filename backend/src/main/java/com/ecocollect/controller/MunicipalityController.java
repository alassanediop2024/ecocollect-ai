package com.ecocollect.controller;

import com.ecocollect.dto.MunicipalityRequest;
import com.ecocollect.dto.MunicipalityResponse;
import com.ecocollect.service.MunicipalityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/municipalities")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    public MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    @GetMapping
    public List<MunicipalityResponse> getAll() {
        return municipalityService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MunicipalityResponse> getById(@PathVariable Long id) {
        return municipalityService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MunicipalityResponse> create(
            @Valid @RequestBody MunicipalityRequest request) {

        MunicipalityResponse created = municipalityService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }
}

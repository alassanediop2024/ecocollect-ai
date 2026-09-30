package com.ecocollect.controller;

import com.ecocollect.dto.AnomalyRequest;
import com.ecocollect.dto.AnomalyResponse;
import com.ecocollect.model.enums.AnomalySeverity;
import com.ecocollect.model.enums.AnomalyStatus;
import com.ecocollect.model.enums.AnomalyType;
import com.ecocollect.service.AnomalyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anomalies")
public class AnomalyController {

    private final AnomalyService anomalyService;

    public AnomalyController(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    @GetMapping
    public List<AnomalyResponse> getAll(
            @RequestParam(required = false) Long containerId,
            @RequestParam(required = false) Long sectorId,
            @RequestParam(required = false) AnomalyStatus status,
            @RequestParam(required = false) AnomalySeverity severity,
            @RequestParam(required = false) AnomalyType type) {

        if (containerId != null) {
            return anomalyService.findByContainer(containerId);
        }

        if (sectorId != null) {
            return anomalyService.findBySector(sectorId);
        }

        if (status != null) {
            return anomalyService.findByStatus(status);
        }

        if (severity != null) {
            return anomalyService.findBySeverity(severity);
        }

        if (type != null) {
            return anomalyService.findByType(type);
        }

        return anomalyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnomalyResponse> getById(@PathVariable Long id) {
        return anomalyService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AnomalyResponse> create(
            @Valid @RequestBody AnomalyRequest request) {

        AnomalyResponse created = anomalyService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<AnomalyResponse> resolve(@PathVariable Long id) {
        return anomalyService.resolve(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

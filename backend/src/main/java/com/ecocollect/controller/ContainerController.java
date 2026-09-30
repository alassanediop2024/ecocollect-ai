package com.ecocollect.controller;

import com.ecocollect.dto.ContainerRequest;
import com.ecocollect.dto.ContainerResponse;
import com.ecocollect.model.enums.ContainerStatus;
import com.ecocollect.service.ContainerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/containers")
public class ContainerController {

    private final ContainerService containerService;

    public ContainerController(ContainerService containerService) {
        this.containerService = containerService;
    }

    @GetMapping
    public List<ContainerResponse> getAll(
            @RequestParam(required = false) Long sectorId,
            @RequestParam(required = false) ContainerStatus status) {

        if (sectorId != null) {
            return containerService.findBySector(sectorId);
        }

        if (status != null) {
            return containerService.findByStatus(status);
        }

        return containerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContainerResponse> getById(@PathVariable Long id) {
        return containerService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ContainerResponse> create(
            @Valid @RequestBody ContainerRequest request) {

        ContainerResponse created = containerService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }
}

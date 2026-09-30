package com.ecocollect.controller;

import com.ecocollect.dto.CollectionRequest;
import com.ecocollect.dto.CollectionResponse;
import com.ecocollect.model.enums.CollectionStatus;
import com.ecocollect.service.CollectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @GetMapping
    public List<CollectionResponse> getAll(
            @RequestParam(required = false) Long containerId,
            @RequestParam(required = false) Long sectorId,
            @RequestParam(required = false) CollectionStatus status) {

        if (containerId != null) {
            return collectionService.findByContainer(containerId);
        }

        if (sectorId != null) {
            return collectionService.findBySector(sectorId);
        }

        if (status != null) {
            return collectionService.findByStatus(status);
        }

        return collectionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionResponse> getById(@PathVariable Long id) {
        return collectionService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CollectionResponse> create(
            @Valid @RequestBody CollectionRequest request) {

        CollectionResponse created = collectionService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }
}

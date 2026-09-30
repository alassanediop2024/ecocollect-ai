package com.ecocollect.dto;

import com.ecocollect.model.enums.CollectionStatus;

import java.time.LocalDateTime;

public record CollectionResponse(
    Long id,
    Long containerId,
    String containerCode,
    Long sectorId,
    String sectorName,
    Long municipalityId,
    String municipalityName,
    LocalDateTime collectionDate,
    Double weightKg,
    CollectionStatus status,
    String operator,
    String notes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

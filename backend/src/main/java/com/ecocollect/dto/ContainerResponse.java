package com.ecocollect.dto;

import com.ecocollect.model.enums.ContainerStatus;
import com.ecocollect.model.enums.ContainerType;

import java.time.LocalDateTime;

public record ContainerResponse(
    Long id,
    String code,
    String address,
    Double latitude,
    Double longitude,
    ContainerType containerType,
    Integer capacity,
    ContainerStatus status,
    Long sectorId,
    String sectorName,
    Long municipalityId,
    String municipalityName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

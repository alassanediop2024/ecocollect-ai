package com.ecocollect.dto;

import com.ecocollect.model.enums.AnomalySeverity;
import com.ecocollect.model.enums.AnomalyStatus;
import com.ecocollect.model.enums.AnomalyType;

import java.time.LocalDateTime;

public record AnomalyResponse(
    Long id,
    Long containerId,
    String containerCode,
    Long sectorId,
    String sectorName,
    Long municipalityId,
    String municipalityName,
    AnomalyType type,
    String description,
    AnomalySeverity severity,
    AnomalyStatus status,
    LocalDateTime reportedAt,
    LocalDateTime resolvedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

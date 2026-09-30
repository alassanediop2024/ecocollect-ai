package com.ecocollect.dto;

import java.time.LocalDateTime;

public record SectorResponse(

    Long id,
    String code,
    String name,
    String description,
    Long municipalityId,
    String municipalityName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {
}

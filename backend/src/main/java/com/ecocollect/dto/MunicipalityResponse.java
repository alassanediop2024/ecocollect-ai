package com.ecocollect.dto;

import java.time.LocalDateTime;

public record MunicipalityResponse(

    Long id,
    String code,
    String name,
    String region,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {
}

package com.ecocollect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SectorRequest(

    @NotBlank(message = "Le code du secteur est obligatoire.")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères.")
    String code,

    @NotBlank(message = "Le nom du secteur est obligatoire.")
    @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères.")
    String name,

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères.")
    String description,

    @NotNull(message = "La municipalité est obligatoire.")
    Long municipalityId

) {
}

package com.ecocollect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MunicipalityRequest(

    @NotBlank(message = "Le code de la municipalité est obligatoire.")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères.")
    String code,

    @NotBlank(message = "Le nom de la municipalité est obligatoire.")
    @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères.")
    String name,

    @Size(max = 150, message = "La région ne doit pas dépasser 150 caractères.")
    String region

) {
}

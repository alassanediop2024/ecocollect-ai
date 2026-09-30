package com.ecocollect.dto;

import com.ecocollect.model.enums.CollectionStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CollectionRequest(

    @NotNull(message = "Le conteneur est obligatoire.")
    Long containerId,

    @NotNull(message = "La date de collecte est obligatoire.")
    LocalDateTime collectionDate,

    @PositiveOrZero(message = "Le poids collecté doit être supérieur ou égal à zéro.")
    Double weightKg,

    CollectionStatus status,

    @Size(max = 150, message = "Le nom de l'opérateur ne doit pas dépasser 150 caractères.")
    String operator,

    @Size(max = 1000, message = "Les notes ne doivent pas dépasser 1000 caractères.")
    String notes
) {}

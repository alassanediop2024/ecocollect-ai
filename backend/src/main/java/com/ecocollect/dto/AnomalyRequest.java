package com.ecocollect.dto;

import com.ecocollect.model.enums.AnomalySeverity;
import com.ecocollect.model.enums.AnomalyStatus;
import com.ecocollect.model.enums.AnomalyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AnomalyRequest(

    @NotNull(message = "Le conteneur est obligatoire.")
    Long containerId,

    @NotNull(message = "Le type d'anomalie est obligatoire.")
    AnomalyType type,

    @NotBlank(message = "La description est obligatoire.")
    @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères.")
    String description,

    @NotNull(message = "Le niveau de gravité est obligatoire.")
    AnomalySeverity severity,

    AnomalyStatus status,

    LocalDateTime reportedAt
) {}

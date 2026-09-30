package com.ecocollect.dto;

import com.ecocollect.model.enums.ContainerStatus;
import com.ecocollect.model.enums.ContainerType;
import jakarta.validation.constraints.*;

public record ContainerRequest(

    @NotBlank(message = "Le code du conteneur est obligatoire.")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères.")
    String code,

    @NotBlank(message = "L'adresse du conteneur est obligatoire.")
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères.")
    String address,

    @DecimalMin(value = "-90.0", message = "La latitude doit être supérieure ou égale à -90.")
    @DecimalMax(value = "90.0", message = "La latitude doit être inférieure ou égale à 90.")
    Double latitude,

    @DecimalMin(value = "-180.0", message = "La longitude doit être supérieure ou égale à -180.")
    @DecimalMax(value = "180.0", message = "La longitude doit être inférieure ou égale à 180.")
    Double longitude,

    @NotNull(message = "Le type de conteneur est obligatoire.")
    ContainerType containerType,

    @NotNull(message = "La capacité est obligatoire.")
    @Positive(message = "La capacité doit être supérieure à zéro.")
    Integer capacity,

    ContainerStatus status,

    @NotNull(message = "Le secteur est obligatoire.")
    Long sectorId
) {}

package com.ecocollect.mapper;

import com.ecocollect.dto.AnomalyResponse;
import com.ecocollect.model.Anomaly;
import org.springframework.stereotype.Component;

@Component
public class AnomalyMapper {

    public AnomalyResponse toResponse(Anomaly anomaly) {
        return new AnomalyResponse(
            anomaly.getId(),
            anomaly.getContainer().getId(),
            anomaly.getContainer().getCode(),
            anomaly.getContainer().getSector().getId(),
            anomaly.getContainer().getSector().getName(),
            anomaly.getContainer().getSector().getMunicipality().getId(),
            anomaly.getContainer().getSector().getMunicipality().getName(),
            anomaly.getType(),
            anomaly.getDescription(),
            anomaly.getSeverity(),
            anomaly.getStatus(),
            anomaly.getReportedAt(),
            anomaly.getResolvedAt(),
            anomaly.getCreatedAt(),
            anomaly.getUpdatedAt()
        );
    }
}

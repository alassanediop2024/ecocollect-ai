package com.ecocollect.dto;

public record DashboardStatisticsResponse(
    long municipalities,
    long sectors,
    long containers,
    long activeContainers,
    long collections,
    double totalWeightKg,
    long reportedAnomalies,
    long inProgressAnomalies,
    long resolvedAnomalies,
    long criticalAnomalies
) {}

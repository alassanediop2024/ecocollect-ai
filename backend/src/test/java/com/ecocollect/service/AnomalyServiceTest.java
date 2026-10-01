package com.ecocollect.service;

import com.ecocollect.dto.AnomalyRequest;
import com.ecocollect.dto.AnomalyResponse;
import com.ecocollect.exception.ResourceNotFoundException;
import com.ecocollect.mapper.AnomalyMapper;
import com.ecocollect.model.Anomaly;
import com.ecocollect.model.Container;
import com.ecocollect.model.Municipality;
import com.ecocollect.model.Sector;
import com.ecocollect.model.enums.AnomalySeverity;
import com.ecocollect.model.enums.AnomalyStatus;
import com.ecocollect.model.enums.AnomalyType;
import com.ecocollect.repository.AnomalyRepository;
import com.ecocollect.repository.ContainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnomalyServiceTest {

    @Mock
    private AnomalyRepository anomalyRepository;

    @Mock
    private ContainerRepository containerRepository;

    private AnomalyService anomalyService;

    @BeforeEach
    void setUp() {
        anomalyService = new AnomalyService(
                anomalyRepository,
                containerRepository,
                new AnomalyMapper()
        );
    }

    @Test
    void shouldCreateAnomaly() {

        Container container = createContainerMock();

        LocalDateTime reportedAt =
                LocalDateTime.of(2026, 9, 30, 20, 0);

        AnomalyRequest request = new AnomalyRequest(
                1L,
                AnomalyType.OVERFLOW,
                "Conteneur plein",
                AnomalySeverity.HIGH,
                AnomalyStatus.REPORTED,
                reportedAt
        );

        when(containerRepository.findById(1L))
                .thenReturn(Optional.of(container));

        when(anomalyRepository.save(any(Anomaly.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AnomalyResponse response =
                anomalyService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.containerId());
        assertEquals("CONT-001", response.containerCode());

        assertEquals(1L, response.sectorId());
        assertEquals("Secteur Centre", response.sectorName());

        assertEquals(1L, response.municipalityId());
        assertEquals(
                "Ville de Démonstration",
                response.municipalityName()
        );

        assertEquals(AnomalyType.OVERFLOW, response.type());
        assertEquals("Conteneur plein", response.description());
        assertEquals(AnomalySeverity.HIGH, response.severity());
        assertEquals(AnomalyStatus.REPORTED, response.status());
        assertEquals(reportedAt, response.reportedAt());

        verify(containerRepository).findById(1L);
        verify(anomalyRepository).save(any(Anomaly.class));
    }

    @Test
    void shouldRejectUnknownContainer() {

        AnomalyRequest request = new AnomalyRequest(
                9999L,
                AnomalyType.DAMAGED,
                "Conteneur inexistant",
                AnomalySeverity.HIGH,
                null,
                null
        );

        when(containerRepository.findById(9999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> anomalyService.create(request)
                );

        assertEquals(
                "Le conteneur avec l'identifiant '9999' n'existe pas.",
                exception.getMessage()
        );

        verify(containerRepository).findById(9999L);

        verify(anomalyRepository, never())
                .save(any(Anomaly.class));
    }

    @Test
    void shouldResolveAnomaly() {

        Container container = createContainerMock();

        Anomaly anomaly = new Anomaly();
        anomaly.setContainer(container);
        anomaly.setType(AnomalyType.DAMAGED);
        anomaly.setDescription("Conteneur endommagé");
        anomaly.setSeverity(AnomalySeverity.MEDIUM);
        anomaly.setStatus(AnomalyStatus.REPORTED);
        anomaly.setReportedAt(
                LocalDateTime.of(2026, 9, 30, 19, 0)
        );

        when(anomalyRepository.findById(2L))
                .thenReturn(Optional.of(anomaly));

        when(anomalyRepository.saveAndFlush(anomaly))
                .thenReturn(anomaly);

        Optional<AnomalyResponse> result =
                anomalyService.resolve(2L);

        assertTrue(result.isPresent());

        AnomalyResponse response = result.get();

        assertEquals(
                AnomalyStatus.RESOLVED,
                response.status()
        );

        assertNotNull(response.resolvedAt());

        assertEquals(
                AnomalyStatus.RESOLVED,
                anomaly.getStatus()
        );

        assertNotNull(anomaly.getResolvedAt());

        verify(anomalyRepository).findById(2L);
        verify(anomalyRepository).saveAndFlush(anomaly);
    }

    @Test
    void shouldFindAnomaliesBySeverity() {

        Container container = createContainerMock();

        Anomaly first = createAnomaly(
                container,
                AnomalyType.OVERFLOW,
                "Conteneur plein"
        );

        Anomaly second = createAnomaly(
                container,
                AnomalyType.DAMAGED,
                "Conteneur endommagé"
        );

        when(anomalyRepository.findBySeverity(
                AnomalySeverity.HIGH))
                .thenReturn(List.of(first, second));

        List<AnomalyResponse> responses =
                anomalyService.findBySeverity(
                        AnomalySeverity.HIGH
                );

        assertEquals(2, responses.size());

        assertEquals(
                AnomalySeverity.HIGH,
                responses.get(0).severity()
        );

        assertEquals(
                AnomalySeverity.HIGH,
                responses.get(1).severity()
        );

        verify(anomalyRepository)
                .findBySeverity(AnomalySeverity.HIGH);
    }

    private Container createContainerMock() {

        Municipality municipality = mock(Municipality.class);
        when(municipality.getId()).thenReturn(1L);
        when(municipality.getName())
                .thenReturn("Ville de Démonstration");

        Sector sector = mock(Sector.class);
        when(sector.getId()).thenReturn(1L);
        when(sector.getName()).thenReturn("Secteur Centre");
        when(sector.getMunicipality())
                .thenReturn(municipality);

        Container container = mock(Container.class);
        when(container.getId()).thenReturn(1L);
        when(container.getCode()).thenReturn("CONT-001");
        when(container.getSector()).thenReturn(sector);

        return container;
    }

    private Anomaly createAnomaly(
            Container container,
            AnomalyType type,
            String description) {

        Anomaly anomaly = new Anomaly();

        anomaly.setContainer(container);
        anomaly.setType(type);
        anomaly.setDescription(description);
        anomaly.setSeverity(AnomalySeverity.HIGH);
        anomaly.setStatus(AnomalyStatus.REPORTED);
        anomaly.setReportedAt(
                LocalDateTime.of(2026, 9, 30, 20, 0)
        );

        return anomaly;
    }
}

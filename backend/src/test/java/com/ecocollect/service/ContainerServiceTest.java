package com.ecocollect.service;

import com.ecocollect.dto.ContainerRequest;
import com.ecocollect.dto.ContainerResponse;
import com.ecocollect.exception.DuplicateResourceException;
import com.ecocollect.exception.ResourceNotFoundException;
import com.ecocollect.mapper.ContainerMapper;
import com.ecocollect.model.Container;
import com.ecocollect.model.Municipality;
import com.ecocollect.model.Sector;
import com.ecocollect.model.enums.ContainerStatus;
import com.ecocollect.model.enums.ContainerType;
import com.ecocollect.repository.ContainerRepository;
import com.ecocollect.repository.SectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContainerServiceTest {

    @Mock
    private ContainerRepository containerRepository;

    @Mock
    private SectorRepository sectorRepository;

    private ContainerService containerService;

    @BeforeEach
    void setUp() {
        containerService = new ContainerService(
                containerRepository,
                sectorRepository,
                new ContainerMapper()
        );
    }

    @Test
    void shouldCreateContainer() {

        Sector sector = createSectorMock();

        ContainerRequest request = new ContainerRequest(
                "CONT-TEST",
                "100 Avenue Centrale",
                48.4284,
                -71.0685,
                ContainerType.RECYCLING,
                1100,
                ContainerStatus.ACTIVE,
                1L
        );

        when(containerRepository.existsByCode("CONT-TEST"))
                .thenReturn(false);

        when(sectorRepository.findById(1L))
                .thenReturn(Optional.of(sector));

        when(containerRepository.save(any(Container.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ContainerResponse response =
                containerService.create(request);

        assertNotNull(response);
        assertEquals("CONT-TEST", response.code());
        assertEquals("100 Avenue Centrale", response.address());
        assertEquals(ContainerType.RECYCLING, response.containerType());
        assertEquals(1100, response.capacity());
        assertEquals(ContainerStatus.ACTIVE, response.status());

        assertEquals(1L, response.sectorId());
        assertEquals("Secteur Centre", response.sectorName());

        assertEquals(1L, response.municipalityId());
        assertEquals(
                "Ville de Démonstration",
                response.municipalityName()
        );

        verify(containerRepository)
                .existsByCode("CONT-TEST");

        verify(sectorRepository)
                .findById(1L);

        verify(containerRepository)
                .save(any(Container.class));
    }

    @Test
    void shouldRejectDuplicateContainerCode() {

        ContainerRequest request = new ContainerRequest(
                "CONT-001",
                "Adresse doublon",
                48.4284,
                -71.0685,
                ContainerType.RECYCLING,
                1100,
                ContainerStatus.ACTIVE,
                1L
        );

        when(containerRepository.existsByCode("CONT-001"))
                .thenReturn(true);

        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () -> containerService.create(request)
                );

        assertEquals(
                "Un conteneur avec le code 'CONT-001' existe déjà.",
                exception.getMessage()
        );

        verify(containerRepository)
                .existsByCode("CONT-001");

        verify(sectorRepository, never())
                .findById(anyLong());

        verify(containerRepository, never())
                .save(any(Container.class));
    }

    @Test
    void shouldRejectUnknownSector() {

        ContainerRequest request = new ContainerRequest(
                "CONT-404",
                "Adresse Test",
                48.4284,
                -71.0685,
                ContainerType.RECYCLING,
                1100,
                ContainerStatus.ACTIVE,
                9999L
        );

        when(containerRepository.existsByCode("CONT-404"))
                .thenReturn(false);

        when(sectorRepository.findById(9999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> containerService.create(request)
                );

        assertEquals(
                "Le secteur avec l'identifiant '9999' n'existe pas.",
                exception.getMessage()
        );

        verify(containerRepository)
                .existsByCode("CONT-404");

        verify(sectorRepository)
                .findById(9999L);

        verify(containerRepository, never())
                .save(any(Container.class));
    }

    @Test
    void shouldFindContainersByStatus() {

        Sector sector = createSectorMock();

        Container first = createContainer(
                "CONT-001",
                ContainerStatus.ACTIVE,
                sector
        );

        Container second = createContainer(
                "CONT-002",
                ContainerStatus.ACTIVE,
                sector
        );

        when(containerRepository.findByStatus(ContainerStatus.ACTIVE))
                .thenReturn(List.of(first, second));

        List<ContainerResponse> responses =
                containerService.findByStatus(ContainerStatus.ACTIVE);

        assertEquals(2, responses.size());

        assertEquals("CONT-001", responses.get(0).code());
        assertEquals(
                ContainerStatus.ACTIVE,
                responses.get(0).status()
        );

        assertEquals("CONT-002", responses.get(1).code());
        assertEquals(
                ContainerStatus.ACTIVE,
                responses.get(1).status()
        );

        verify(containerRepository)
                .findByStatus(ContainerStatus.ACTIVE);
    }

    private Sector createSectorMock() {

        Municipality municipality = mock(Municipality.class);

        when(municipality.getId()).thenReturn(1L);
        when(municipality.getName())
                .thenReturn("Ville de Démonstration");

        Sector sector = mock(Sector.class);

        when(sector.getId()).thenReturn(1L);
        when(sector.getName()).thenReturn("Secteur Centre");
        when(sector.getMunicipality()).thenReturn(municipality);

        return sector;
    }

    private Container createContainer(
            String code,
            ContainerStatus status,
            Sector sector) {

        Container container = new Container();

        container.setCode(code);
        container.setAddress("Adresse " + code);
        container.setLatitude(48.4284);
        container.setLongitude(-71.0685);
        container.setContainerType(ContainerType.RECYCLING);
        container.setCapacity(1100);
        container.setStatus(status);
        container.setSector(sector);

        return container;
    }
}

package com.ecocollect.service;

import com.ecocollect.dto.MunicipalityRequest;
import com.ecocollect.dto.MunicipalityResponse;
import com.ecocollect.exception.DuplicateResourceException;
import com.ecocollect.mapper.MunicipalityMapper;
import com.ecocollect.model.Municipality;
import com.ecocollect.repository.MunicipalityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MunicipalityServiceTest {

    @Mock
    private MunicipalityRepository municipalityRepository;

    private MunicipalityService municipalityService;

    @BeforeEach
    void setUp() {
        MunicipalityMapper municipalityMapper = new MunicipalityMapper();

        municipalityService = new MunicipalityService(
                municipalityRepository,
                municipalityMapper
        );
    }

    @Test
    void shouldCreateMunicipality() {

        MunicipalityRequest request = new MunicipalityRequest(
                "MUN-TEST",
                "Municipalité Test",
                "Région Test"
        );

        when(municipalityRepository.existsByCode("MUN-TEST"))
                .thenReturn(false);

        when(municipalityRepository.save(any(Municipality.class)))
                .thenAnswer(invocation -> {
                    Municipality municipality = invocation.getArgument(0);
                    municipality.setId(1L);
                    return municipality;
                });

        MunicipalityResponse response =
                municipalityService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("MUN-TEST", response.code());
        assertEquals("Municipalité Test", response.name());
        assertEquals("Région Test", response.region());

        verify(municipalityRepository).existsByCode("MUN-TEST");
        verify(municipalityRepository).save(any(Municipality.class));
    }

    @Test
    void shouldRejectDuplicateMunicipalityCode() {

        MunicipalityRequest request = new MunicipalityRequest(
                "MUN-001",
                "Municipalité en doublon",
                "Région Test"
        );

        when(municipalityRepository.existsByCode("MUN-001"))
                .thenReturn(true);

        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () -> municipalityService.create(request)
                );

        assertEquals(
                "Une municipalité avec le code 'MUN-001' existe déjà.",
                exception.getMessage()
        );

        verify(municipalityRepository).existsByCode("MUN-001");
        verify(municipalityRepository, never())
                .save(any(Municipality.class));
    }

    @Test
    void shouldReturnAllMunicipalities() {

        Municipality first = new Municipality();
        first.setId(1L);
        first.setCode("MUN-001");
        first.setName("Ville de Démonstration");
        first.setRegion("Région pilote");

        Municipality second = new Municipality();
        second.setId(2L);
        second.setCode("MUN-002");
        second.setName("Municipalité Test");
        second.setRegion("Région Nord");

        when(municipalityRepository.findAll())
                .thenReturn(List.of(first, second));

        List<MunicipalityResponse> responses =
                municipalityService.findAll();

        assertEquals(2, responses.size());

        assertEquals("MUN-001", responses.get(0).code());
        assertEquals("Ville de Démonstration", responses.get(0).name());

        assertEquals("MUN-002", responses.get(1).code());
        assertEquals("Municipalité Test", responses.get(1).name());

        verify(municipalityRepository).findAll();
    }
}

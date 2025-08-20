package es.dsrroma.school.springboot.integracionbase.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.dsrroma.school.springboot.integracionbase.dtos.SalaDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;
import es.dsrroma.school.springboot.integracionbase.mappers.SalaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Sala;
import es.dsrroma.school.springboot.integracionbase.repositories.SalaRepository;

class SalaServiceImplTest {

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SalaServiceImpl salaService;

    private Sala sala;
    private SalaDTO salaDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        sala = new Sala("A1", "Sala de reuniones", 10);
        salaDTO = SalaMapper.toDTO(sala);
    }

    @Test
    public void testFindSalaById_Exists() throws EntityNotFoundException {
        // Arrange
        when(salaRepository.findById("A1")).thenReturn(Optional.of(sala));

        // Act
        SalaDTO result = salaService.findSalaById("A1");

        // Assert
        assertNotNull(result);
        assertEquals(sala.getId(), result.getId());
        assertEquals(sala.getDescripcion(), result.getDescripcion());
        verify(salaRepository, times(1)).findById("A1");
    }

    @Test
    public void testFindSalaById_NotFound() {
        // Arrange
        when(salaRepository.findById("A1")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salaService.findSalaById("A1");
        });
        assertEquals("Sala con ID A1 no encontrada.", exception.getMessage());
    }

    @Test
    public void testCreateSala() {
        // Arrange
        when(salaRepository.save(any(Sala.class))).thenReturn(sala);

        // Act
        SalaDTO result = salaService.createSala(salaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(sala.getId(), result.getId());
        verify(salaRepository, times(1)).save(any(Sala.class));
    }

    @Test
    public void testUpdateSala() throws EntityNotFoundException {
        // Arrange
        when(salaRepository.findById("A1")).thenReturn(Optional.of(sala));
        when(salaRepository.save(any(Sala.class))).thenReturn(sala);

        // Act
        salaDTO.setDescripcion("Sala de conferencias");
        SalaDTO result = salaService.updateSala("A1", salaDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Sala de conferencias", result.getDescripcion());
        verify(salaRepository, times(1)).findById("A1");
        verify(salaRepository, times(1)).save(any(Sala.class));
    }

    @Test
    public void testDeleteSala() throws EntityNotFoundException {
        // Arrange
        when(salaRepository.existsById("A1")).thenReturn(true);

        // Act
        salaService.deleteSala("A1");

        // Assert
        verify(salaRepository, times(1)).deleteById("A1");
    }

    @Test
    public void testDeleteSala_NotFound() {
        // Arrange
        when(salaRepository.existsById("A1")).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            salaService.deleteSala("A1");
        });
        assertEquals("Sala con ID A1 no encontrada.", exception.getMessage());
    }

    @Test
    public void testFindSalasWithCapacityGreaterThanEqual() {
        // Arrange
        when(salaRepository.findByCapacidadGreaterThanEqual(10)).thenReturn(Set.of(sala));

        // Act
        List<SalaDTO> result = salaService.findSalasWithCapacityGreaterThanEqual(10);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(salaRepository, times(1)).findByCapacidadGreaterThanEqual(10);
    }

    @Test
    public void testFindSalasAdequateForCapacity() {
        // Arrange
        when(salaRepository.findLast3ByCapacidadBetween(10, 20)).thenReturn(Set.of(sala));

        // Act
        List<SalaDTO> result = salaService.findSalasAdequateForCapacity(10);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(salaRepository, times(1)).findLast3ByCapacidadBetween(10, 20);
    }

    @Test
    public void testFindSalasAulas() {
        // Arrange
        when(salaRepository.encontrarAulas()).thenReturn(Set.of(sala));

        // Act
        List<SalaDTO> result = salaService.findSalasAulas();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(salaRepository, times(1)).encontrarAulas();
    }
}

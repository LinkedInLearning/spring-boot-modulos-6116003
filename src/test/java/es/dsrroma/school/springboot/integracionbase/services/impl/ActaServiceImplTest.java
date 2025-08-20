package es.dsrroma.school.springboot.integracionbase.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;
import es.dsrroma.school.springboot.integracionbase.mappers.ActaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Acta;
import es.dsrroma.school.springboot.integracionbase.repositories.ActaRepository;

class ActaServiceImplTest {

    @Mock
    private ActaRepository actaRepository;

    @InjectMocks
    private ActaServiceImpl actaService;

    private Acta acta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Crear un ejemplo de Acta para los tests
        acta = new Acta();
        acta.setId(1L);
        acta.setContenido("Contenido del acta de ejemplo.");
    }

    @Test
    void testFindActaById_Success() {
        // Arrange
        when(actaRepository.findById(1L)).thenReturn(java.util.Optional.of(acta));

        // Act
        ActaDTO result = actaService.findActaById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Contenido del acta de ejemplo.", result.getContenido());
    }

    @Test
    void testFindActaById_EntityNotFound() {
        // Arrange
        when(actaRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            actaService.findActaById(1L);
        });
        assertEquals("Acta con ID 1 no encontrada.", exception.getMessage());
    }

    @Test
    void testCreateActa_Success() {
        // Arrange
        ActaDTO actaDTO = new ActaDTO();
        actaDTO.setContenido("Contenido nuevo del acta.");
        Acta savedActa = ActaMapper.toEntity(actaDTO);
        when(actaRepository.save(any(Acta.class))).thenReturn(savedActa);

        // Act
        ActaDTO result = actaService.createActa(actaDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Contenido nuevo del acta.", result.getContenido());
    }

    @Test
    void testUpdateActa_Success() {
        // Arrange
        ActaDTO updatedDTO = new ActaDTO();
        updatedDTO.setContenido("Contenido actualizado");
        when(actaRepository.findById(1L)).thenReturn(java.util.Optional.of(acta));
        when(actaRepository.save(any(Acta.class))).thenReturn(acta);

        // Act
        ActaDTO result = actaService.updateActa(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Contenido actualizado", result.getContenido());
    }

    @Test
    void testUpdateActa_EntityNotFound() {
        // Arrange
        ActaDTO updatedDTO = new ActaDTO();
        updatedDTO.setContenido("Contenido actualizado");
        when(actaRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            actaService.updateActa(1L, updatedDTO);
        });
        assertEquals("Acta con ID 1 no encontrada.", exception.getMessage());
    }

    @Test
    void testDeleteActa_Success() {
        // Arrange
        when(actaRepository.existsById(1L)).thenReturn(true);

        // Act
        actaService.deleteActa(1L);

        // Assert
        verify(actaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteActa_EntityNotFound() {
        // Arrange
        when(actaRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            actaService.deleteActa(1L);
        });
        assertEquals("Acta con ID 1 no encontrada.", exception.getMessage());
    }
}

package es.dsrroma.school.springboot.integracionbase.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.dsrroma.school.springboot.integracionbase.dtos.PersonaDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;
import es.dsrroma.school.springboot.integracionbase.mappers.PersonaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Persona;
import es.dsrroma.school.springboot.integracionbase.repositories.PersonaRepository;

class PersonaServiceImplTest {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaServiceImpl personaService;

    private Persona persona;
    private PersonaDTO personaDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        persona = new Persona();
        persona.setId(1L);
        persona.setNumeroEmpleado("12345");
        persona.setNombre("John");
        persona.setApellidos("Doe");
        personaDTO = PersonaMapper.toDTO(persona);
    }

    @Test
    public void testFindPersonaById_Exists() throws EntityNotFoundException {
        // Arrange
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        // Act
        PersonaDTO result = personaService.findPersonaById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(persona.getId(), result.getId());
        assertEquals(persona.getNumeroEmpleado(), result.getNumeroEmpleado());
        verify(personaRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindPersonaById_NotFound() {
        // Arrange
        when(personaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personaService.findPersonaById(1L);
        });
        assertEquals("Persona con ID 1 no encontrada.", exception.getMessage());
    }

    @Test
    public void testCreatePersona() {
        // Arrange
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        // Act
        PersonaDTO result = personaService.createPersona(personaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(persona.getId(), result.getId());
        verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    public void testUpdatePersona() throws EntityNotFoundException {
        // Arrange
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        // Act
        personaDTO.setNombre("Jane");
        PersonaDTO result = personaService.updatePersona(1L, personaDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getNombre());
        verify(personaRepository, times(1)).findById(1L);
        verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    public void testDeletePersona() throws EntityNotFoundException {
        // Arrange
        when(personaRepository.existsById(1L)).thenReturn(true);

        // Act
        personaService.deletePersona(1L);

        // Assert
        verify(personaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePersona_NotFound() {
        // Arrange
        when(personaRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personaService.deletePersona(1L);
        });
        assertEquals("Persona con ID 1 no encontrada.", exception.getMessage());
    }
}

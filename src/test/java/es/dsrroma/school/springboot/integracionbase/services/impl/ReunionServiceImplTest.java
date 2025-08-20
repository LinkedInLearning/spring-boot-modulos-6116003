package es.dsrroma.school.springboot.integracionbase.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.dtos.ReunionDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;
import es.dsrroma.school.springboot.integracionbase.mappers.ReunionMapper;
import es.dsrroma.school.springboot.integracionbase.models.Acta;
import es.dsrroma.school.springboot.integracionbase.models.Persona;
import es.dsrroma.school.springboot.integracionbase.models.Reunion;
import es.dsrroma.school.springboot.integracionbase.models.Sala;
import es.dsrroma.school.springboot.integracionbase.repositories.ActaRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.PersonaRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.ReunionRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.SalaRepository;

class ReunionServiceImplTest {

	@Mock
	private ReunionRepository reunionRepository;

	@Mock
	private SalaRepository salaRepository;

	@Mock
	private PersonaRepository personaRepository;

	@Mock
	private ActaRepository actaRepository;

	@InjectMocks
	private ReunionServiceImpl reunionService;

	private Sala sala;
	private Persona persona;

	private Reunion reunion;
	private ReunionDTO reunionDTO;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		// Setup the mock objects
		sala = new Sala("A1", "Sala de conferencias", 50);
        persona = new Persona();
        persona.setId(1L);
        persona.setNumeroEmpleado("12345");
        persona.setNombre("John");
        persona.setApellidos("Doe");
		reunion = new Reunion(1L, "Reunión de prueba", Instant.now(), sala, null, Set.of(persona));

		reunionDTO = ReunionMapper.toDTO(reunion);
	}

	@Test
	public void testFindReunionById_Exists() throws EntityNotFoundException {
		// Arrange
		when(reunionRepository.findById(1L)).thenReturn(java.util.Optional.of(reunion));

		// Act
		ReunionDTO result = reunionService.findReunionById(1L);

		// Assert
		assertNotNull(result);
		assertEquals(reunion.getId(), result.getId());
		verify(reunionRepository, times(1)).findById(1L);
	}

	@Test
	public void testFindReunionById_NotFound() {
		// Arrange
		when(reunionRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		// Act & Assert
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			reunionService.findReunionById(1L);
		});
		assertEquals("Reunion con ID 1 no encontrada.", exception.getMessage());
	}

	@Test
	public void testCreateReunion() throws EntityNotFoundException {
		// Arrange
		when(salaRepository.findById("A1")).thenReturn(java.util.Optional.of(sala));
		when(reunionRepository.save(any(Reunion.class))).thenReturn(reunion);
		when(personaRepository.findById(1L)).thenReturn(java.util.Optional.of(persona));

		// Act
		ReunionDTO result = reunionService.createReunion(reunionDTO);

		// Assert
		assertNotNull(result);
		assertEquals(reunion.getId(), result.getId());
		verify(salaRepository, times(1)).findById("A1");
		verify(reunionRepository, times(2)).save(any(Reunion.class));
		verify(personaRepository, times(1)).findById(1L);
	}

	@Test
	public void testUpdateReunion() throws EntityNotFoundException {
		// Arrange
		when(reunionRepository.findById(1L)).thenReturn(java.util.Optional.of(reunion));
		when(reunionRepository.save(any(Reunion.class))).thenReturn(reunion);

		// Act
		reunionDTO.setAsunto("Nuevo asunto");
		ReunionDTO result = reunionService.updateReunion(1L, reunionDTO);

		// Assert
		assertNotNull(result);
		assertEquals("Nuevo asunto", result.getAsunto());
		verify(reunionRepository, times(1)).findById(1L);
		verify(reunionRepository, times(1)).save(any(Reunion.class));
	}

	@Test
	public void testDeleteReunion() throws EntityNotFoundException {
		// Arrange
		when(reunionRepository.existsById(1L)).thenReturn(true);

		// Act
		reunionService.deleteReunion(1L);

		// Assert
		verify(reunionRepository, times(1)).deleteById(1L);
	}

	@Test
	public void testDeleteReunion_NotFound() {
		// Arrange
		when(reunionRepository.existsById(1L)).thenReturn(false);

		// Act & Assert
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			reunionService.deleteReunion(1L);
		});
		assertEquals("Reunion con ID 1 no encontrada.", exception.getMessage());
	}

	@Test
	public void testAddSalaToReunion() throws EntityNotFoundException {
		// Arrange
		when(reunionRepository.findById(1L)).thenReturn(java.util.Optional.of(reunion));
		when(salaRepository.findById("A1")).thenReturn(java.util.Optional.of(sala));

		// Act
		reunionService.addSalaToReunion(1L, "A1");

		// Assert
		verify(reunionRepository, times(1)).save(any(Reunion.class));
		assertEquals(sala, reunion.getSala());
	}

	@Test
	public void testAddActaToReunion() throws EntityNotFoundException {
	    // Arrange
	    ActaDTO actaDTO = new ActaDTO();
	    actaDTO.setContenido("Contenido del acta");

	    // Mock de los repositorios
	    Reunion reunion = 
	    		new Reunion(1L, "Reunión de prueba", Instant.now(), sala, null, new HashSet<>());
	    Acta actaMock = new Acta();
	    actaMock.setContenido("Contenido del acta");

	    when(reunionRepository.findById(1L)).thenReturn(java.util.Optional.of(reunion));
	    when(actaRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(actaMock));
	    when(actaRepository.save(any(Acta.class))).thenReturn(actaMock);
	    
	    // Act
	    reunionService.addActaToReunion(1L, actaDTO);

	    // Assert
	    // Verifica que se haya guardado la reunión
	    verify(reunionRepository, times(1)).save(any(Reunion.class)); 
	    // Verifica que la reunión tenga un acta asignada
	    assertNotNull(reunion.getActa()); 
	    // Verifica que el contenido del acta sea correcto
	    assertEquals("Contenido del acta", reunion.getActa().getContenido());
	}

	@Test
	public void testAddParticipantes() throws EntityNotFoundException {
	    // Arrange
	    Set<Long> participantesIds = Set.of(1L); // ID de participante
	    Reunion reunion = 
	    		new Reunion(1L, "Reunión de prueba", Instant.now(), sala, null, new HashSet<>());
	    Persona persona = new Persona();
        persona.setId(1L);
        persona.setNumeroEmpleado("12345");
        persona.setNombre("John");
        persona.setApellidos("Doe");
	    
	    when(reunionRepository.findById(1L)).thenReturn(java.util.Optional.of(reunion));
	    when(personaRepository.findById(1L)).thenReturn(java.util.Optional.of(persona));

	    // Act
	    reunionService.addParticipantes(1L, participantesIds);

	    // Assert
	    verify(reunionRepository, times(1)).save(any(Reunion.class));
	    assertTrue(reunion.getParticipantes().contains(persona));
	}

	@Test
	public void testAddParticipantes_ParticipanteNotFound() {
		// Arrange
		Set<Long> participantesIds = Set.of(1L);
		when(reunionRepository.findById(1L)).thenReturn(java.util.Optional.of(reunion));
		when(personaRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		// Act & Assert
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			reunionService.addParticipantes(1L, participantesIds);
		});
		assertEquals("Persona con ID [1] no encontrada.", exception.getMessage());
	}
}

package es.dsrroma.school.springboot.integracionbase.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import es.dsrroma.school.springboot.integracionbase.dtos.PersonaDTO;
import es.dsrroma.school.springboot.integracionbase.mappers.PersonaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Persona;
import es.dsrroma.school.springboot.integracionbase.repositories.PersonaRepository;

@RestController
@RequestMapping("/personas")
public class PersonaController {

	private final PersonaRepository personaRepository;

	private PersonaController(PersonaRepository personaRepository) {
		this.personaRepository = personaRepository;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<PersonaDTO> findById(@PathVariable Long requestedId) {
		Optional<Persona> personaOpt = personaRepository.findById(requestedId);
		if (personaOpt.isPresent()) {
			PersonaDTO dto = PersonaMapper.toDTO(personaOpt.get());
			return ResponseEntity.ok(dto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	private ResponseEntity<Iterable<PersonaDTO>> findAll() {
		Iterable<Persona> personas = personaRepository.findAll();

		List<PersonaDTO> dtos = StreamSupport.stream(personas.spliterator(), false)
				.map(PersonaMapper::toDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}

	@PostMapping
	private ResponseEntity<Void> createPersona(@RequestBody PersonaDTO newPersonaRequest, 
			UriComponentsBuilder ucb) {
		Persona persona = PersonaMapper.toEntity(newPersonaRequest);
		Persona savedPersona = personaRepository.save(persona);
		URI locationOfNewPersona = ucb.path("personas/{id}")
				.buildAndExpand(savedPersona.getId()).toUri();
		return ResponseEntity.created(locationOfNewPersona).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putPersona(@PathVariable Long requestedId, 
			@RequestBody PersonaDTO personaUpdate) {
		Optional<Persona> persona = personaRepository.findById(requestedId);
		if (persona.isPresent()) {
			Persona updatedPersona = new Persona(
					persona.get().getId(), 
					personaUpdate.getNumeroEmpleado(),
					personaUpdate.getNombre(), 
					personaUpdate.getApellidos());
			personaRepository.save(updatedPersona);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deletePersona(@PathVariable Long id) {
		if (personaRepository.existsById(id)) {
			personaRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}

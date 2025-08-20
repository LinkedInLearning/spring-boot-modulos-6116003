package es.dsrroma.school.springboot.integracionbase.controllers;

import java.net.URI;
import java.util.List;

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
import es.dsrroma.school.springboot.integracionbase.services.PersonaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/personas")
public class PersonaController {

	private final PersonaService personaService;

	private PersonaController(PersonaService personaService) {
		this.personaService = personaService;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<PersonaDTO> findById(@PathVariable Long requestedId) {
		PersonaDTO personaDTO = personaService.findPersonaById(requestedId);
		if (personaDTO != null) {
			return ResponseEntity.ok(personaDTO);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	private ResponseEntity<List<PersonaDTO>> findAll() {
		List<PersonaDTO> personas = personaService.findAllPersonas();
		return ResponseEntity.ok(personas);
	}

	@PostMapping
	private ResponseEntity<Void> createPersona(@Valid @RequestBody PersonaDTO newPersonaRequest,
			UriComponentsBuilder ucb) {
		PersonaDTO savedPersona = personaService.createPersona(newPersonaRequest);
		URI locationOfNewPersona = ucb.path("personas/{id}")
				.buildAndExpand(savedPersona.getId()).toUri();
		return ResponseEntity.created(locationOfNewPersona).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putPersona(@PathVariable Long requestedId,
			@Valid @RequestBody PersonaDTO personaUpdate) {
		PersonaDTO updatedPersona = personaService.updatePersona(requestedId, personaUpdate);
		if (updatedPersona != null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deletePersona(@PathVariable Long id) {
		boolean deleted = personaService.deletePersona(id);
		if (deleted) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}

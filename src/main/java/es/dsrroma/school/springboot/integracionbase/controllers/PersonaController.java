package es.dsrroma.school.springboot.integracionbase.controllers;

import java.net.URI;
import java.util.Optional;

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
	private ResponseEntity<Persona> findById(@PathVariable Long requestedId) {
		Optional<Persona> personaOpt = personaRepository.findById(requestedId);
		if (personaOpt.isPresent()) {
			return ResponseEntity.ok(personaOpt.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	private ResponseEntity<Iterable<Persona>> findAll() {
		return ResponseEntity.ok(personaRepository.findAll());
	}

	@PostMapping
	private ResponseEntity<Void> createPersona(@RequestBody Persona newPersonaRequest, UriComponentsBuilder ucb) {
		Persona savedPersona = personaRepository.save(newPersonaRequest);
		URI locationOfNewPersona = ucb.path("personas/{id}").buildAndExpand(savedPersona.getId()).toUri();
		return ResponseEntity.created(locationOfNewPersona).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putPersona(@PathVariable Long requestedId, @RequestBody Persona personaUpdate) {
		Optional<Persona> persona = personaRepository.findById(requestedId);
		if (persona.isPresent()) {
			Persona updatedPersona = new Persona(persona.get().getId(), personaUpdate.getNumeroEmpleado(), 
					personaUpdate.getNombre(), personaUpdate.getApellidos());
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

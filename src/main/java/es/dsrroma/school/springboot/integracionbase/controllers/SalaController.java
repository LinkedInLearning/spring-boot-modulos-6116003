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

import es.dsrroma.school.springboot.integracionbase.models.Sala;
import es.dsrroma.school.springboot.integracionbase.repositories.SalaRepository;

@RestController
@RequestMapping("/salas")
public class SalaController {

	private final SalaRepository salaRepository;

	private SalaController(SalaRepository salaRepository) {
		this.salaRepository = salaRepository;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<Sala> findById(@PathVariable String requestedId) {
		Optional<Sala> salaOpt = salaRepository.findById(requestedId);
		if (salaOpt.isPresent()) {
			return ResponseEntity.ok(salaOpt.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	private ResponseEntity<Iterable<Sala>> findAll() {
		return ResponseEntity.ok(salaRepository.findAll());
	}

	@PostMapping
	private ResponseEntity<Void> createSala(@RequestBody Sala newSalaRequest, UriComponentsBuilder ucb) {
		Sala savedSala = salaRepository.save(newSalaRequest);
		URI locationOfNewSala = ucb.path("salas/{id}").buildAndExpand(savedSala.getId()).toUri();
		return ResponseEntity.created(locationOfNewSala).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putSala(@PathVariable String requestedId, @RequestBody Sala salaUpdate) {
		Optional<Sala> sala = salaRepository.findById(requestedId);
		if (sala.isPresent()) {
			Sala updatedSala = new Sala(sala.get().getId(), salaUpdate.getDescripcion(), salaUpdate.getCapacidad());
			salaRepository.save(updatedSala);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deleteSala(@PathVariable String id) {
		if (salaRepository.existsById(id)) {
			salaRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}

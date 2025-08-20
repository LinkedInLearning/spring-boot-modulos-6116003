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

import es.dsrroma.school.springboot.integracionbase.models.Acta;
import es.dsrroma.school.springboot.integracionbase.repositories.ActaRepository;

@RestController
@RequestMapping("/actas")
public class ActaController {

	private final ActaRepository actaRepository;

	private ActaController(ActaRepository actaRepository) {
		this.actaRepository = actaRepository;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<Acta> findById(@PathVariable Long requestedId) {
		Optional<Acta> actaOpt = actaRepository.findById(requestedId);
		if (actaOpt.isPresent()) {
			return ResponseEntity.ok(actaOpt.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	private ResponseEntity<Iterable<Acta>> findAll() {
		return ResponseEntity.ok(actaRepository.findAll());
	}

	@PostMapping
	private ResponseEntity<Void> createActa(@RequestBody Acta newActaRequest, UriComponentsBuilder ucb) {
		Acta savedActa = actaRepository.save(newActaRequest);
		URI locationOfNewActa = ucb.path("actas/{id}").buildAndExpand(savedActa.getId()).toUri();
		return ResponseEntity.created(locationOfNewActa).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putActa(@PathVariable Long requestedId, @RequestBody Acta actaUpdate) {
		Optional<Acta> acta = actaRepository.findById(requestedId);
		if (acta.isPresent()) {
			Acta updatedActa = new Acta(acta.get().getId(), actaUpdate.getContenido());
			actaRepository.save(updatedActa);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deleteActa(@PathVariable Long id) {
		if (actaRepository.existsById(id)) {
			actaRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}

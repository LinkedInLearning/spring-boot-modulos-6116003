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

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.services.ActaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/actas")
public class ActaController {

	private final ActaService actaService;

	private ActaController(ActaService actaService) {
		this.actaService = actaService;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<ActaDTO> findById(@PathVariable Long requestedId) {
		ActaDTO actaDTO = actaService.findActaById(requestedId);
		if (actaDTO != null) {
			return ResponseEntity.ok(actaDTO);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	private ResponseEntity<List<ActaDTO>> findAll() {
		List<ActaDTO> actas = actaService.findAllActas();
		return ResponseEntity.ok(actas);
	}

	@PostMapping
	private ResponseEntity<Void> createActa(@Valid @RequestBody ActaDTO newActaRequest, 
			UriComponentsBuilder ucb) {
		ActaDTO savedActa = actaService.createActa(newActaRequest);
		URI locationOfNewActa = ucb.path("actas/{id}").buildAndExpand(savedActa.getId()).toUri();
		return ResponseEntity.created(locationOfNewActa).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putActa(@PathVariable Long requestedId, 
			@Valid @RequestBody ActaDTO actaUpdate) {
		ActaDTO updatedActa = actaService.updateActa(requestedId, actaUpdate);
		if (updatedActa != null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deleteActa(@PathVariable Long id) {
		boolean deleted = actaService.deleteActa(id);
		if (deleted) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}

package es.dsrroma.school.springboot.integracionbase.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import es.dsrroma.school.springboot.integracionbase.dtos.SalaDTO;
import es.dsrroma.school.springboot.integracionbase.services.SalaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/salas")
@CrossOrigin(origins = "https://localhost:4200")
public class SalaController {

	private final SalaService salaService;

	private SalaController(SalaService salaService) {
		this.salaService = salaService;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<SalaDTO> findById(@PathVariable String requestedId) {
		SalaDTO salaDTO = salaService.findSalaById(requestedId);
		return ResponseEntity.ok(salaDTO);
	}

	@GetMapping
	private ResponseEntity<List<SalaDTO>> findAll() {
		List<SalaDTO> salas = salaService.findAllSalas();
		return ResponseEntity.ok(salas);
	}

	@PostMapping
	private ResponseEntity<Void> createSala(@Valid @RequestBody SalaDTO newSalaRequest, 
			UriComponentsBuilder ucb) {
		SalaDTO savedSala = salaService.createSala(newSalaRequest);
		URI locationOfNewSala = ucb.path("salas/{id}").buildAndExpand(savedSala.getId()).toUri();
		return ResponseEntity.created(locationOfNewSala).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putSala(@PathVariable String requestedId, 
			@Valid @RequestBody SalaDTO salaUpdate) {
		salaService.updateSala(requestedId, salaUpdate);
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deleteSala(@PathVariable String id) {
		salaService.deleteSala(id);
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/para/{num}")
	private ResponseEntity<List<SalaDTO>> findPara(@PathVariable int num) {
		List<SalaDTO> salas = salaService.findSalasWithCapacityGreaterThanEqual(num);
		return ResponseEntity.ok(salas);
	}

	@GetMapping("/adecuada/{num}")
	private ResponseEntity<List<SalaDTO>> findAdecuadas(@PathVariable int num) {
		List<SalaDTO> salas = salaService.findSalasAdequateForCapacity(num);
		return ResponseEntity.ok(salas);
	}

	@GetMapping("/aulas")
	private ResponseEntity<List<SalaDTO>> findAulas() {
		List<SalaDTO> salas = salaService.findSalasAulas();
		return ResponseEntity.ok(salas);
	}
}

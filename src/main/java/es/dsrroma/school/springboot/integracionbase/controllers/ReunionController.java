package es.dsrroma.school.springboot.integracionbase.controllers;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
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
import es.dsrroma.school.springboot.integracionbase.dtos.ReunionDTO;
import es.dsrroma.school.springboot.integracionbase.services.ReunionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/reuniones")
public class ReunionController {

	private final ReunionService reunionService;

	private ReunionController(ReunionService reunionService) {
		this.reunionService = reunionService;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<ReunionDTO> findById(@PathVariable Long requestedId) {
		ReunionDTO reunionDTO = reunionService.findReunionById(requestedId);
		return ResponseEntity.ok(reunionDTO);
	}

	@GetMapping
	private ResponseEntity<List<ReunionDTO>> findAll() {
		List<ReunionDTO> reuniones = reunionService.findAllReuniones();
		return ResponseEntity.ok(reuniones);
	}

	@GetMapping("/page")
	private ResponseEntity<List<ReunionDTO>> findAll(Pageable pageable) {
		return ResponseEntity.ok(reunionService.findAllReuniones(pageable));
	}

	@GetMapping("/page2")
	private ResponseEntity<List<ReunionDTO>> findAll2(Pageable pageable) {
		return ResponseEntity.ok(reunionService.findAllReuniones2(pageable));
	}

	@PostMapping
	private ResponseEntity<Void> createReunion(@Valid @RequestBody ReunionDTO newReunionRequest,
			UriComponentsBuilder ucb) {
		ReunionDTO savedReunion = reunionService.createReunion(newReunionRequest);
		URI locationOfNewReunion = ucb.path("reuniones/{id}")
				.buildAndExpand(savedReunion.getId()).toUri();
		return ResponseEntity.created(locationOfNewReunion).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putReunion(@PathVariable Long requestedId,
			@Valid @RequestBody ReunionDTO reunionUpdate) {
		reunionService.updateReunion(requestedId, reunionUpdate);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deleteReunion(@PathVariable Long id) {
		reunionService.deleteReunion(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{reunionId}/sala/{salaId}")
	public ResponseEntity<Void> addSalaToReunion(@PathVariable Long reunionId, 
			@PathVariable String salaId) {
		reunionService.addSalaToReunion(reunionId, salaId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{reunionId}/acta")
	public ResponseEntity<Void> addActaToReunion(@PathVariable Long reunionId, 
			@Valid @RequestBody ActaDTO actaRequest) {
		reunionService.addActaToReunion(reunionId, actaRequest);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{reunionId}/participantes")
	public ResponseEntity<Void> addParticipantes(@PathVariable Long reunionId,
			@Valid @RequestBody Set<Long> participantesIds) {
		reunionService.addParticipantes(reunionId, participantesIds);
		return ResponseEntity.noContent().build();
	}
}

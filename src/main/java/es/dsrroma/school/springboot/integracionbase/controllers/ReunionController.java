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

	public ReunionController(ReunionService reunionService) {
		this.reunionService = reunionService;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<ReunionDTO> findById(@PathVariable Long requestedId) {
		ReunionDTO reunionDTO = reunionService.findReunionById(requestedId);
		if (reunionDTO != null) {
			return ResponseEntity.ok(reunionDTO);
		} else {
			return ResponseEntity.notFound().build();
		}
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
		if (savedReunion != null) {
			URI locationOfNewReunion = ucb.path("reuniones/{id}")
					.buildAndExpand(savedReunion.getId()).toUri();
			return ResponseEntity.created(locationOfNewReunion).build();
		}
		return ResponseEntity.badRequest().build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putReunion(@PathVariable Long requestedId,
			@Valid @RequestBody ReunionDTO reunionUpdate) {
		ReunionDTO updatedReunion = reunionService.updateReunion(requestedId, reunionUpdate);
		if (updatedReunion != null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deleteReunion(@PathVariable Long id) {
		boolean deleted = reunionService.deleteReunion(id);
		if (deleted) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{reunionId}/sala/{salaId}")
	public ResponseEntity<Void> addSalaToReunion(@PathVariable Long reunionId, 
			@PathVariable String salaId) {
		boolean updated = reunionService.addSalaToReunion(reunionId, salaId);
		if (updated) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{reunionId}/acta")
	public ResponseEntity<Void> addActaToReunion(@PathVariable Long reunionId,
			@Valid @RequestBody ActaDTO actaRequest) {
		boolean updated = reunionService.addActaToReunion(reunionId, actaRequest);
		if (updated) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{reunionId}/participantes")
	public ResponseEntity<Void> addParticipantes(@PathVariable Long reunionId,
			@Valid @RequestBody Set<Long> participantesIds) {
		boolean updated = reunionService.addParticipantes(reunionId, participantesIds);
		if (updated) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}

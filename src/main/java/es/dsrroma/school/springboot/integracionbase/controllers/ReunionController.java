package es.dsrroma.school.springboot.integracionbase.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import es.dsrroma.school.springboot.integracionbase.models.Reunion;
import es.dsrroma.school.springboot.integracionbase.repositories.ReunionRepository;

@RestController
@RequestMapping("/reuniones")
public class ReunionController {

	private final ReunionRepository reunionRepository;

	private ReunionController(ReunionRepository reunionRepository) {
		this.reunionRepository = reunionRepository;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<Reunion> findById(@PathVariable Long requestedId) {
		Optional<Reunion> reunionOpt = reunionRepository.findById(requestedId);
		if (reunionOpt.isPresent()) {
			return ResponseEntity.ok(reunionOpt.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	private ResponseEntity<Iterable<Reunion>> findAll() {
		return ResponseEntity.ok(reunionRepository.findAll());
	}

	@GetMapping("/page")
	private ResponseEntity<List<Reunion>> findAll(Pageable pageable) {
		Page<Reunion> page = reunionRepository.findAll(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				pageable.getSortOr(Sort.by(Sort.Direction.ASC, "fecha"))));
		return ResponseEntity.ok(page.getContent());
	}

	@PostMapping
	private ResponseEntity<Void> createReunion(@RequestBody Reunion newReunionRequest, 
			UriComponentsBuilder ucb) {
		Reunion savedReunion = reunionRepository.save(newReunionRequest);
		URI locationOfNewReunion = 
				ucb.path("reuniones/{id}").buildAndExpand(savedReunion.getId()).toUri();
		return ResponseEntity.created(locationOfNewReunion).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putReunion(@PathVariable Long requestedId, 
			@RequestBody Reunion reunionUpdate) {
		Optional<Reunion> reunion = reunionRepository.findById(requestedId);
		if (reunion.isPresent()) {
			Reunion updatedReunion = new Reunion(reunion.get().getId(), 
					reunionUpdate.getAsunto(), reunionUpdate.getFecha());
			reunionRepository.save(updatedReunion);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deleteReunion(@PathVariable Long id) {
		if (reunionRepository.existsById(id)) {
			reunionRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}

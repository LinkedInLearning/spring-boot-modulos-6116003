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

import es.dsrroma.school.springboot.integracionbase.dtos.SalaDTO;
import es.dsrroma.school.springboot.integracionbase.mappers.SalaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Sala;
import es.dsrroma.school.springboot.integracionbase.repositories.SalaRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/salas")
public class SalaController {

	private final SalaRepository salaRepository;

	private SalaController(SalaRepository salaRepository) {
		this.salaRepository = salaRepository;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<SalaDTO> findById(@PathVariable String requestedId) {
		Optional<Sala> salaOpt = salaRepository.findById(requestedId);
		if (salaOpt.isPresent()) {
			SalaDTO dto = SalaMapper.toDTO(salaOpt.get());
			return ResponseEntity.ok(dto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	private ResponseEntity<Iterable<SalaDTO>> findAll() {
		Iterable<Sala> salas = salaRepository.findAll();

		List<SalaDTO> dtos = StreamSupport.stream(salas.spliterator(), false)
				.map(SalaMapper::toDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}

	@PostMapping
	private ResponseEntity<Void> createSala(@Valid @RequestBody SalaDTO newSalaRequest, 
			UriComponentsBuilder ucb) {
		Sala sala = SalaMapper.toEntity(newSalaRequest);
		Sala savedSala = salaRepository.save(sala);
		URI locationOfNewSala = ucb.path("salas/{id}")
				.buildAndExpand(savedSala.getId()).toUri();
		return ResponseEntity.created(locationOfNewSala).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putSala(@PathVariable String requestedId, 
			@Valid @RequestBody SalaDTO salaUpdate) {
		Optional<Sala> sala = salaRepository.findById(requestedId);
		if (sala.isPresent()) {
			Sala updatedSala = new Sala(sala.get().getId(), salaUpdate.getDescripcion(), 
					salaUpdate.getCapacidad());
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
	
	@GetMapping("/para/{num}")
	private ResponseEntity<Iterable<SalaDTO>> findPara(int num) {
		Iterable<Sala> salas = salaRepository.findByCapacidadGreaterThanEqual(num);

		List<SalaDTO> dtos = StreamSupport.stream(salas.spliterator(), false)
				.map(SalaMapper::toDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/adecuada/{num}")
	private ResponseEntity<Iterable<SalaDTO>> findAdecuadas(int num) {
		Iterable<Sala> salas = salaRepository.findLast3ByCapacidadBetween(num, num * 2);

		List<SalaDTO> dtos = StreamSupport.stream(salas.spliterator(), false)
				.map(SalaMapper::toDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}
}

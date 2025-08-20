package es.dsrroma.school.springboot.integracionbase.controllers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

import es.dsrroma.school.springboot.integracionbase.models.Acta;
import es.dsrroma.school.springboot.integracionbase.models.Persona;
import es.dsrroma.school.springboot.integracionbase.models.Reunion;
import es.dsrroma.school.springboot.integracionbase.models.Sala;
import es.dsrroma.school.springboot.integracionbase.repositories.ActaRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.PersonaRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.ReunionRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.SalaRepository;

@RestController
@RequestMapping("/reuniones")
public class ReunionController {

	private final ReunionRepository reunionRepository;
	private final SalaRepository salaRepository;
	private final PersonaRepository personaRepository;
	private final ActaRepository actaRepository;

	private ReunionController(ReunionRepository reunionRepository, SalaRepository salaRepository,
			PersonaRepository personaRepository, ActaRepository actaRepository) {
		this.reunionRepository = reunionRepository;
		this.salaRepository = salaRepository;
		this.personaRepository = personaRepository;
		this.actaRepository = actaRepository;
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

	@PutMapping("/{reunionId}/sala/{salaId}")
	private ResponseEntity<Void> addSalaToReunion(@PathVariable Long reunionId, 
			@PathVariable String salaId) {
		Optional<Reunion> reunionOpt = reunionRepository.findById(reunionId);

		if (reunionOpt.isEmpty()) {
			return ResponseEntity.notFound().build(); // Si no se encuentra la reunión
		}

		Reunion reunion = reunionOpt.get();

		Sala sala = salaRepository.findById(salaId).orElse(null);
		if (sala == null) {
			return ResponseEntity.notFound().build(); // Si no se encuentra la sala
		}
		reunion.setSala(sala);

		reunionRepository.save(reunion);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{reunionId}/acta")
	private ResponseEntity<Void> addActaToReunion(@PathVariable Long reunionId, 
			@RequestBody Acta actaRequest) {
		// Buscar la reunión
		Optional<Reunion> reunionOpt = reunionRepository.findById(reunionId);

		if (reunionOpt.isEmpty()) {
			// Si no se encuentra la reunión, devolver 404
			return ResponseEntity.notFound().build();
		}

		Reunion reunion = reunionOpt.get();

		Acta acta;

		if (actaRequest.getId() != null) { // acta existente
			acta = actaRepository.findById(actaRequest.getId()).orElse(null);
			if (acta == null) {
				return ResponseEntity.notFound().build();
			}
		} else { // acta nueva
			acta = new Acta();
			acta.setContenido(actaRequest.getContenido());
		}
		acta = actaRepository.save(acta);

		reunion.setActa(acta);

		reunionRepository.save(reunion);

		return ResponseEntity.noContent().build();
	}
    
	/**
	 * Para añadir participantes a una reunión existente.
	 * 
	 * @param reunionId        id de la reunión
	 * @param participantesIds conjunto de ids de los participantes
	 * @return
	 */
	@PutMapping("/{reunionId}/participantes")
	private ResponseEntity<Void> addParticipantes(@PathVariable Long reunionId,
			@RequestBody Set<Long> participantesIds) {
		Optional<Reunion> reunionOpt = reunionRepository.findById(reunionId);

		if (reunionOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Reunion reunion = reunionOpt.get();

		Set<Persona> participantes = participantesIds.stream()
				.map(personaId -> personaRepository.findById(personaId).orElse(null))
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());

		if (participantes.size() != participantesIds.size()) {
			// si hay menos participantes de los esperados
			return ResponseEntity.notFound().build();
		}

		reunion.getParticipantes().addAll(participantes);

		reunionRepository.save(reunion);

		return ResponseEntity.noContent().build();
	}
}

package es.dsrroma.school.springboot.integracionbase.controllers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.dtos.ReunionDTO;
import es.dsrroma.school.springboot.integracionbase.mappers.ReunionMapper;
import es.dsrroma.school.springboot.integracionbase.models.Acta;
import es.dsrroma.school.springboot.integracionbase.models.Persona;
import es.dsrroma.school.springboot.integracionbase.models.Reunion;
import es.dsrroma.school.springboot.integracionbase.models.Sala;
import es.dsrroma.school.springboot.integracionbase.repositories.ActaRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.PersonaRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.ReunionRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.SalaRepository;
import jakarta.validation.Valid;

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
	private ResponseEntity<ReunionDTO> findById(@PathVariable Long requestedId) {
		Optional<Reunion> reunionOpt = reunionRepository.findById(requestedId);
		if (reunionOpt.isPresent()) {
			ReunionDTO dto = ReunionMapper.toDTO(reunionOpt.get());
			return ResponseEntity.ok(dto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	private ResponseEntity<Iterable<ReunionDTO>> findAll() {
		Iterable<Reunion> reuniones = reunionRepository.findAll();

		List<ReunionDTO> dtos = StreamSupport.stream(reuniones.spliterator(), false)
				.map(ReunionMapper::toDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}

	@GetMapping("/page")
	private ResponseEntity<List<ReunionDTO>> findAll(Pageable pageable) {
		Page<Reunion> page = reunionRepository.findAll(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
						pageable.getSortOr(Sort.by(Sort.Direction.ASC, "fecha"))));
		List<ReunionDTO> dtos = page.getContent().stream()
				.map(ReunionMapper::toDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}

	@PostMapping
	private ResponseEntity<Void> createReunion(@Valid @RequestBody ReunionDTO newReunionRequest, 
			UriComponentsBuilder ucb) {
		Reunion reunion = ReunionMapper.toEntity(newReunionRequest);

		// Cargar Acta y Sala
		if (newReunionRequest.getActaId() != null) {
			Acta acta = actaRepository.findById(newReunionRequest.getActaId()).orElse(null);
			if (acta == null) {
				return ResponseEntity.notFound().build();
			}
			reunion.setActa(acta);
		}

		// Cargar Sala
		if (newReunionRequest.getSalaId() != null) {
			Sala sala = salaRepository.findById(newReunionRequest.getSalaId()).orElse(null);
			if (sala == null) {
				return ResponseEntity.notFound().build();
			}
			reunion.setSala(sala);
		}

		// Guardar la reunion para que tenga un id generado
		reunion = reunionRepository.save(reunion);

		// Cargar Participantes
		if (newReunionRequest.getParticipantesIds() != null && 
				!newReunionRequest.getParticipantesIds().isEmpty()) {
			Set<Persona> participantes = newReunionRequest.getParticipantesIds().stream()
					.map(personaId -> personaRepository.findById(personaId).orElse(null))
					.collect(Collectors.toSet());
			if (participantes.size() != newReunionRequest.getParticipantesIds().size()) { 
				// si hay menos participantes de los esperados
				return ResponseEntity.notFound().build();
			}
			reunion.setParticipantes(participantes);
		}

		reunionRepository.save(reunion);
		URI locationOfNewReunion = ucb.path("reuniones/{id}")
				.buildAndExpand(reunion.getId()).toUri();
		return ResponseEntity.created(locationOfNewReunion).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putReunion(@PathVariable Long requestedId, 
			@Valid @RequestBody ReunionDTO reunionUpdate) {
		Optional<Reunion> reunion = reunionRepository.findById(requestedId);
		if (reunion.isPresent()) {
			Reunion updatedReunion = new Reunion(reunion.get().getId(), reunionUpdate.getAsunto(),
					reunionUpdate.getFecha(), null, null, null);

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
	public ResponseEntity<Void> addSalaToReunion(@PathVariable Long reunionId, 
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
	public ResponseEntity<Void> addActaToReunion(@PathVariable Long reunionId, 
			@Valid @RequestBody ActaDTO actaRequest) {
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
	public ResponseEntity<Void> addParticipantes(@PathVariable Long reunionId,
			@Valid @RequestBody Set<Long> participantesIds) {
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

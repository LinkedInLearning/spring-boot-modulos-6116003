package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

@Service
public class ReunionService {

	private final ReunionRepository reunionRepository;
	private final SalaRepository salaRepository;
	private final PersonaRepository personaRepository;
	private final ActaRepository actaRepository;

	public ReunionService(ReunionRepository reunionRepository, SalaRepository salaRepository,
			PersonaRepository personaRepository, ActaRepository actaRepository) {
		this.reunionRepository = reunionRepository;
		this.salaRepository = salaRepository;
		this.personaRepository = personaRepository;
		this.actaRepository = actaRepository;
	}

	public ReunionDTO findReunionById(Long requestedId) {
		Optional<Reunion> reunionOpt = reunionRepository.findById(requestedId);
		return reunionOpt.map(ReunionMapper::toDTO).orElse(null);
	}

	public List<ReunionDTO> findAllReuniones() {
		Iterable<Reunion> reuniones = reunionRepository.findAll();
		return StreamSupport.stream(reuniones.spliterator(), false)
				.map(ReunionMapper::toDTO).collect(Collectors.toList());
	}

	public List<ReunionDTO> findAllReuniones(Pageable pageable) {
		Iterable<Reunion> reuniones = reunionRepository.findAll(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
						pageable.getSortOr(Sort.by(Sort.Direction.ASC, "fecha"))));
		return StreamSupport.stream(reuniones.spliterator(), false)
				.map(ReunionMapper::toDTO).collect(Collectors.toList());
	}

	public List<ReunionDTO> findAllReuniones2(Pageable pageable) {
		Iterable<Reunion> reuniones = reunionRepository.findAll(pageable);
		return StreamSupport.stream(reuniones.spliterator(), false)
				.map(ReunionMapper::toDTO).collect(Collectors.toList());
	}

	public ReunionDTO createReunion(ReunionDTO newReunionRequest) {
		Reunion reunion = ReunionMapper.toEntity(newReunionRequest);

		// Cargar Acta
		if (newReunionRequest.getActaId() != null) {
			Acta acta = actaRepository.findById(newReunionRequest.getActaId()).orElse(null);
			if (acta == null) {
				return null;
			}
			reunion.setActa(acta);
		}

		// Cargar Sala
		if (newReunionRequest.getSalaId() != null) {
			Sala sala = salaRepository.findById(newReunionRequest.getSalaId()).orElse(null);
			if (sala == null) {
				return null;
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
					.filter(Objects::nonNull)
					.collect(Collectors.toSet());

			if (participantes.size() != newReunionRequest.getParticipantesIds().size()) {
				// si hay menos participantes de los esperados, no devolvemos reunión, para
				// luego dar el 404
				return null;
			}
			reunion.setParticipantes(participantes);
		}

		reunionRepository.save(reunion);

		return ReunionMapper.toDTO(reunion);
	}

	public ReunionDTO updateReunion(Long requestedId, ReunionDTO reunionUpdate) {
		Optional<Reunion> reunionOpt = reunionRepository.findById(requestedId);
		if (reunionOpt.isPresent()) {
			Reunion reunion = reunionOpt.get();
			reunion.setAsunto(reunionUpdate.getAsunto());
			reunion.setFecha(reunionUpdate.getFecha());
			reunionRepository.save(reunion);
			return ReunionMapper.toDTO(reunion);
		}
		return null;
	}

	public boolean deleteReunion(Long id) {
		if (reunionRepository.existsById(id)) {
			reunionRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public boolean addSalaToReunion(Long reunionId, String salaId) {
		Optional<Reunion> reunionOpt = reunionRepository.findById(reunionId);
		if (reunionOpt.isEmpty()) {
			return false;
		}

		Reunion reunion = reunionOpt.get();

		Sala sala = salaRepository.findById(salaId).orElse(null);
		if (sala == null) {
			return false;
		}
		reunion.setSala(sala);

		reunionRepository.save(reunion);
		return true;
	}

	public boolean addActaToReunion(Long reunionId, ActaDTO actaRequest) {
		Optional<Reunion> reunionOpt = reunionRepository.findById(reunionId);

		if (reunionOpt.isEmpty()) {
			return false;
		}

		Reunion reunion = reunionOpt.get();

		Acta acta;

		if (actaRequest.getId() != null) {
			acta = actaRepository.findById(actaRequest.getId()).orElse(null);
			if (acta == null) {
				return false;
			}
		} else { // acta nueva
			acta = new Acta();
			acta.setContenido(actaRequest.getContenido());
		}
		acta = actaRepository.save(acta);

		reunion.setActa(acta);

		reunionRepository.save(reunion);

		return true;
	}

	public boolean addParticipantes(Long reunionId, Set<Long> participantesIds) {
		Optional<Reunion> reunionOpt = reunionRepository.findById(reunionId);

		if (reunionOpt.isEmpty()) {
			return false;
		}

		Reunion reunion = reunionOpt.get();

		Set<Persona> participantes = participantesIds.stream()
				.map(personaId -> personaRepository.findById(personaId).orElse(null))
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());

		if (participantes.size() != participantesIds.size()) {
			return false;
		}

		reunion.getParticipantes().addAll(participantes);

		reunionRepository.save(reunion);

		return true;
	}
}

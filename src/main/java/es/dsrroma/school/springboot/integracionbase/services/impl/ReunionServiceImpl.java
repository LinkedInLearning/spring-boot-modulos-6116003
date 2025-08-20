package es.dsrroma.school.springboot.integracionbase.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.dtos.ReunionDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;
import es.dsrroma.school.springboot.integracionbase.mappers.ReunionMapper;
import es.dsrroma.school.springboot.integracionbase.models.Acta;
import es.dsrroma.school.springboot.integracionbase.models.Persona;
import es.dsrroma.school.springboot.integracionbase.models.Reunion;
import es.dsrroma.school.springboot.integracionbase.models.Sala;
import es.dsrroma.school.springboot.integracionbase.repositories.ActaRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.PersonaRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.ReunionRepository;
import es.dsrroma.school.springboot.integracionbase.repositories.SalaRepository;
import es.dsrroma.school.springboot.integracionbase.services.ReunionService;
import jakarta.transaction.Transactional;

@Service
public class ReunionServiceImpl implements ReunionService {

	private final ReunionRepository reunionRepository;
	private final SalaRepository salaRepository;
	private final PersonaRepository personaRepository;
	private final ActaRepository actaRepository;

	public ReunionServiceImpl(ReunionRepository reunionRepository, SalaRepository salaRepository,
			PersonaRepository personaRepository, ActaRepository actaRepository) {
		this.reunionRepository = reunionRepository;
		this.salaRepository = salaRepository;
		this.personaRepository = personaRepository;
		this.actaRepository = actaRepository;
	}

	@Override
	public ReunionDTO findReunionById(Long requestedId) {
		Reunion reunion = reunionRepository.findById(requestedId)
				.orElseThrow(() -> new EntityNotFoundException("Reunion", requestedId));
		return ReunionMapper.toDTO(reunion);
	}

	@Override
	public List<ReunionDTO> findAllReuniones() {
		Iterable<Reunion> reuniones = reunionRepository.findAll();
		return StreamSupport.stream(reuniones.spliterator(), false).map(ReunionMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<ReunionDTO> findAllReuniones(Pageable pageable) {
		Iterable<Reunion> reuniones = reunionRepository.findAll(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
						pageable.getSortOr(Sort.by(Sort.Direction.ASC, "fecha"))));
		return StreamSupport.stream(reuniones.spliterator(), false)
				.map(ReunionMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<ReunionDTO> findAllReuniones2(Pageable pageable) {
		Iterable<Reunion> reuniones = reunionRepository.findAll(pageable);
		return StreamSupport.stream(reuniones.spliterator(), false)
				.map(ReunionMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ReunionDTO createReunion(ReunionDTO newReunionRequest) {
		Reunion reunion = ReunionMapper.toEntity(newReunionRequest);

		// Cargar Acta
		if (newReunionRequest.getActaId() != null) {
			Acta acta = actaRepository.findById(newReunionRequest.getActaId())
					.orElseThrow(() -> new EntityNotFoundException("Acta", 
							newReunionRequest.getActaId()));
			reunion.setActa(acta);
		}

		// Cargar Sala
		if (newReunionRequest.getSalaId() != null) {
			Sala sala = salaRepository.findById(newReunionRequest.getSalaId())
					.orElseThrow(() -> new EntityNotFoundException("Sala", 
							newReunionRequest.getSalaId()));
			reunion.setSala(sala);
		}

		// Guardar la reunion para que tenga un id generado
		reunion = reunionRepository.save(reunion);

		// Cargar Participantes
		Set<Persona> participantes = preparaParticipantes(newReunionRequest.getParticipantesIds());
		reunion.setParticipantes(participantes);

		reunionRepository.save(reunion);

		return ReunionMapper.toDTO(reunion);
	}

	@Override
	public ReunionDTO updateReunion(Long requestedId, ReunionDTO reunionUpdate) {
		Reunion reunion = reunionRepository.findById(requestedId)
				.orElseThrow(() -> new EntityNotFoundException("Reunion", requestedId));

		reunion.setAsunto(reunionUpdate.getAsunto());
		reunion.setFecha(reunionUpdate.getFecha());
		reunionRepository.save(reunion);

		return ReunionMapper.toDTO(reunion);
	}

	@Override
	public void deleteReunion(Long id) {
		if (!reunionRepository.existsById(id)) {
			throw new EntityNotFoundException("Reunion", id);
		}
		reunionRepository.deleteById(id);
	}

	@Override
	public void addSalaToReunion(Long reunionId, String salaId) {
		Reunion reunion = reunionRepository.findById(reunionId)
				.orElseThrow(() -> new EntityNotFoundException("Reunion", reunionId));

		Sala sala = salaRepository.findById(salaId).orElseThrow(() 
				-> new EntityNotFoundException("Sala", salaId));

		reunion.setSala(sala);
		reunionRepository.save(reunion);
	}

	@Override
	public void addActaToReunion(Long reunionId, ActaDTO actaRequest) {
		Reunion reunion = reunionRepository.findById(reunionId)
				.orElseThrow(() -> new EntityNotFoundException("Reunion", reunionId));

		Acta acta;
		if (actaRequest.getId() != null) {
			acta = actaRepository.findById(actaRequest.getId())
					.orElseThrow(() -> new EntityNotFoundException("Acta", actaRequest.getId()));
		} else {
			acta = new Acta();
			acta.setContenido(actaRequest.getContenido());
		}

		acta = actaRepository.save(acta);
		reunion.setActa(acta);

		reunionRepository.save(reunion);
	}

	@Override
	public void addParticipantes(Long reunionId, Set<Long> participantesIds) {
		Reunion reunion = reunionRepository.findById(reunionId)
				.orElseThrow(() -> new EntityNotFoundException("Reunion", reunionId));

		Set<Persona> participantes = preparaParticipantes(participantesIds);

		reunion.getParticipantes().addAll(participantes);
		reunionRepository.save(reunion);
	}

	private Set<Persona> preparaParticipantes(Set<Long> participantesIds) {
		final List<Long> idsFallidos = new ArrayList<>();
		Set<Persona> participantes = participantesIds.stream().map(personaId -> {
			return personaRepository.findById(personaId).orElseGet(() -> {
				// si no encontramos alguna persona, lo anotamos
				idsFallidos.add(personaId);
				return null;
			});
		}).collect(Collectors.toSet());

		if (!idsFallidos.isEmpty()) {
			// si no se encontraron todas las personas, indicamos cuales faltaron
			throw new EntityNotFoundException("Persona", idsFallidos);
		}
		return participantes;
	}
}

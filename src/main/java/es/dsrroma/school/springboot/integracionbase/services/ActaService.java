package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;
import es.dsrroma.school.springboot.integracionbase.mappers.ActaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Acta;
import es.dsrroma.school.springboot.integracionbase.repositories.ActaRepository;

@Service
public class ActaService {

	private final ActaRepository actaRepository;

	public ActaService(ActaRepository actaRepository) {
		this.actaRepository = actaRepository;
	}

	public ActaDTO findActaById(Long requestedId) throws EntityNotFoundException {
		Acta acta = actaRepository.findById(requestedId)
				.orElseThrow(() -> new EntityNotFoundException("Acta", requestedId));
		return ActaMapper.toDTO(acta);
	}

	public List<ActaDTO> findAllActas() {
		Iterable<Acta> actas = actaRepository.findAll();
		return StreamSupport.stream(actas.spliterator(), false)
				.map(ActaMapper::toDTO).collect(Collectors.toList());
	}

	public ActaDTO createActa(ActaDTO newActaRequest) {
		Acta acta = ActaMapper.toEntity(newActaRequest);
		Acta savedActa = actaRepository.save(acta);
		return ActaMapper.toDTO(savedActa);
	}

	public ActaDTO updateActa(Long requestedId, ActaDTO actaUpdate) throws EntityNotFoundException {
		Acta acta = actaRepository.findById(requestedId)
				.orElseThrow(() -> new EntityNotFoundException("Acta", requestedId));
		acta.setContenido(actaUpdate.getContenido());
		Acta savedActa = actaRepository.save(acta);
		return ActaMapper.toDTO(savedActa);
	}

	public void deleteActa(Long id) throws EntityNotFoundException {
		if (!actaRepository.existsById(id)) {
			throw new EntityNotFoundException("Acta", id);
		}
		actaRepository.deleteById(id);
	}
}

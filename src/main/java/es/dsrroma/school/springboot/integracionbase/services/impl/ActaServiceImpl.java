package es.dsrroma.school.springboot.integracionbase.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;
import es.dsrroma.school.springboot.integracionbase.mappers.ActaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Acta;
import es.dsrroma.school.springboot.integracionbase.repositories.ActaRepository;
import es.dsrroma.school.springboot.integracionbase.services.ActaService;

@Service
public class ActaServiceImpl implements ActaService {

	private final ActaRepository actaRepository;

	public ActaServiceImpl(ActaRepository actaRepository) {
		this.actaRepository = actaRepository;
	}

	@Override
	public ActaDTO findActaById(Long requestedId) {
		Acta acta = actaRepository.findById(requestedId)
				.orElseThrow(() -> new EntityNotFoundException("Acta", requestedId));
		return ActaMapper.toDTO(acta);
	}

	@Override
	public List<ActaDTO> findAllActas() {
		Iterable<Acta> actas = actaRepository.findAll();
		return StreamSupport.stream(actas.spliterator(), false)
				.map(ActaMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public ActaDTO createActa(ActaDTO newActaRequest) {
		Acta acta = ActaMapper.toEntity(newActaRequest);
		Acta savedActa = actaRepository.save(acta);
		return ActaMapper.toDTO(savedActa);
	}

	@Override
	public ActaDTO updateActa(Long requestedId, ActaDTO actaUpdate) {
		Acta acta = actaRepository.findById(requestedId)
				.orElseThrow(() -> new EntityNotFoundException("Acta", requestedId));
		acta.setContenido(actaUpdate.getContenido());
		Acta savedActa = actaRepository.save(acta);
		return ActaMapper.toDTO(savedActa);
	}

	@Override
	public void deleteActa(Long id) {
		if (!actaRepository.existsById(id)) {
			throw new EntityNotFoundException("Acta", id);
		}
		actaRepository.deleteById(id);
	}
}

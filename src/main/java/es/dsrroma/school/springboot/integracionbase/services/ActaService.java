package es.dsrroma.school.springboot.integracionbase.services;

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.mappers.ActaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Acta;
import es.dsrroma.school.springboot.integracionbase.repositories.ActaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ActaService {

	private final ActaRepository actaRepository;

	public ActaService(ActaRepository actaRepository) {
		this.actaRepository = actaRepository;
	}

	public ActaDTO findActaById(Long requestedId) {
		Optional<Acta> actaOpt = actaRepository.findById(requestedId);
		return actaOpt.map(ActaMapper::toDTO).orElse(null);
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

	public ActaDTO updateActa(Long requestedId, ActaDTO actaUpdate) {
		Optional<Acta> actaOpt = actaRepository.findById(requestedId);
		if (actaOpt.isPresent()) {
			Acta updatedActa = actaOpt.get();
			updatedActa.setContenido(actaUpdate.getContenido());
			Acta savedActa = actaRepository.save(updatedActa);
			return ActaMapper.toDTO(savedActa);
		}
		return null;
	}

	public boolean deleteActa(Long id) {
		if (actaRepository.existsById(id)) {
			actaRepository.deleteById(id);
			return true;
		}
		return false;
	}
}

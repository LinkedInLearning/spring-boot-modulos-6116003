package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import es.dsrroma.school.springboot.integracionbase.dtos.SalaDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;
import es.dsrroma.school.springboot.integracionbase.mappers.SalaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Sala;
import es.dsrroma.school.springboot.integracionbase.repositories.SalaRepository;

@Service
public class SalaService {

	private final SalaRepository salaRepository;

	public SalaService(SalaRepository salaRepository) {
		this.salaRepository = salaRepository;
	}

	public SalaDTO findSalaById(String requestedId) throws EntityNotFoundException {
		Optional<Sala> salaOpt = salaRepository.findById(requestedId);
		return salaOpt.map(SalaMapper::toDTO).orElseThrow(() 
				-> new EntityNotFoundException("Sala", requestedId));
	}

	public List<SalaDTO> findAllSalas() {
		Iterable<Sala> salas = salaRepository.findAll();
		return StreamSupport.stream(salas.spliterator(), false)
				.map(SalaMapper::toDTO).collect(Collectors.toList());
	}

	public SalaDTO createSala(SalaDTO newSalaRequest) {
		Sala sala = SalaMapper.toEntity(newSalaRequest);
		Sala savedSala = salaRepository.save(sala);
		return SalaMapper.toDTO(savedSala);
	}

	public SalaDTO updateSala(String requestedId, SalaDTO salaUpdate) 
			throws EntityNotFoundException {
		Sala updatedSala = salaRepository.findById(requestedId)
				.orElseThrow(() -> new EntityNotFoundException("Sala", requestedId));
		updatedSala.setDescripcion(salaUpdate.getDescripcion());
		updatedSala.setCapacidad(salaUpdate.getCapacidad());
		Sala savedSala = salaRepository.save(updatedSala);
		return SalaMapper.toDTO(savedSala);
	}

	public void deleteSala(String id) throws EntityNotFoundException {
		if (!salaRepository.existsById(id)) {
			throw new EntityNotFoundException("Sala", id);
		}
		salaRepository.deleteById(id);
	}

	public List<SalaDTO> findSalasWithCapacityGreaterThanEqual(int num) {
		Iterable<Sala> salas = salaRepository.findByCapacidadGreaterThanEqual(num);
		return StreamSupport.stream(salas.spliterator(), false)
				.map(SalaMapper::toDTO).collect(Collectors.toList());
	}

	public List<SalaDTO> findSalasAdequateForCapacity(int num) {
		Iterable<Sala> salas = salaRepository.findLast3ByCapacidadBetween(num, num * 2);
		return StreamSupport.stream(salas.spliterator(), false)
				.map(SalaMapper::toDTO).collect(Collectors.toList());
	}

	public List<SalaDTO> findSalasAulas() {
		Iterable<Sala> salas = salaRepository.encontrarAulas();
		return StreamSupport.stream(salas.spliterator(), false)
				.map(SalaMapper::toDTO).collect(Collectors.toList());
	}
}

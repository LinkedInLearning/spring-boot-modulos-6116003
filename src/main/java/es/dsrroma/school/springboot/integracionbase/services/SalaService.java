package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import es.dsrroma.school.springboot.integracionbase.dtos.SalaDTO;
import es.dsrroma.school.springboot.integracionbase.mappers.SalaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Sala;
import es.dsrroma.school.springboot.integracionbase.repositories.SalaRepository;

@Service
public class SalaService {

	private final SalaRepository salaRepository;

	public SalaService(SalaRepository salaRepository) {
		this.salaRepository = salaRepository;
	}

	public SalaDTO findSalaById(String requestedId) {
		Optional<Sala> salaOpt = salaRepository.findById(requestedId);
		return salaOpt.map(SalaMapper::toDTO).orElse(null);
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

	public SalaDTO updateSala(String requestedId, SalaDTO salaUpdate) {
		Optional<Sala> salaOpt = salaRepository.findById(requestedId);
		if (salaOpt.isPresent()) {
			Sala updatedSala = salaOpt.get();
			updatedSala.setDescripcion(salaUpdate.getDescripcion());
			updatedSala.setCapacidad(salaUpdate.getCapacidad());
			Sala savedSala = salaRepository.save(updatedSala);
			return SalaMapper.toDTO(savedSala);
		}
		return null;
	}

	public boolean deleteSala(String id) {
		if (salaRepository.existsById(id)) {
			salaRepository.deleteById(id);
			return true;
		}
		return false;
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

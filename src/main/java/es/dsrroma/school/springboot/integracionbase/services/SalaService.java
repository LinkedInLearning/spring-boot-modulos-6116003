package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;

import es.dsrroma.school.springboot.integracionbase.dtos.SalaDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;

public interface SalaService {

	SalaDTO findSalaById(String requestedId) throws EntityNotFoundException;

	List<SalaDTO> findAllSalas();

	SalaDTO createSala(SalaDTO newSalaRequest);

	SalaDTO updateSala(String requestedId, SalaDTO salaUpdate) throws EntityNotFoundException;

	void deleteSala(String id) throws EntityNotFoundException;

	List<SalaDTO> findSalasWithCapacityGreaterThanEqual(int num);

	List<SalaDTO> findSalasAdequateForCapacity(int num);

	List<SalaDTO> findSalasAulas();
}

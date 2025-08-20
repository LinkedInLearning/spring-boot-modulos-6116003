package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;

import es.dsrroma.school.springboot.integracionbase.dtos.SalaDTO;

public interface SalaService {

	SalaDTO findSalaById(String requestedId);

	List<SalaDTO> findAllSalas();

	SalaDTO createSala(SalaDTO newSalaRequest);

	SalaDTO updateSala(String requestedId, SalaDTO salaUpdate);

	void deleteSala(String id);

	List<SalaDTO> findSalasWithCapacityGreaterThanEqual(int num);

	List<SalaDTO> findSalasAdequateForCapacity(int num);

	List<SalaDTO> findSalasAulas();
}

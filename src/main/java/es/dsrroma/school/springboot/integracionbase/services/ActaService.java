package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;

public interface ActaService {

	ActaDTO findActaById(Long requestedId);

	List<ActaDTO> findAllActas();

	ActaDTO createActa(ActaDTO newActaRequest);

	ActaDTO updateActa(Long requestedId, ActaDTO actaUpdate);

	void deleteActa(Long id);
}

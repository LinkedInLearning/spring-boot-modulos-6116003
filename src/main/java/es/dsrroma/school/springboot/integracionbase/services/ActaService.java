package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;

public interface ActaService {

	ActaDTO findActaById(Long requestedId) throws EntityNotFoundException;

	List<ActaDTO> findAllActas();

	ActaDTO createActa(ActaDTO newActaRequest);

	ActaDTO updateActa(Long requestedId, ActaDTO actaUpdate) throws EntityNotFoundException;

	void deleteActa(Long id) throws EntityNotFoundException;
}

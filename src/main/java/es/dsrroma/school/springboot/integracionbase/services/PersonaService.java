package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;

import es.dsrroma.school.springboot.integracionbase.dtos.PersonaDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;

public interface PersonaService {

	PersonaDTO findPersonaById(Long requestedId) throws EntityNotFoundException;

	List<PersonaDTO> findAllPersonas();

	PersonaDTO createPersona(PersonaDTO newPersonaRequest);

	PersonaDTO updatePersona(Long requestedId, PersonaDTO personaUpdate) throws EntityNotFoundException;

	void deletePersona(Long id) throws EntityNotFoundException;
}

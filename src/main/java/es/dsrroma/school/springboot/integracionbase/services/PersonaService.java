package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;

import es.dsrroma.school.springboot.integracionbase.dtos.PersonaDTO;

public interface PersonaService {

	PersonaDTO findPersonaById(Long requestedId);

	List<PersonaDTO> findAllPersonas();

	PersonaDTO createPersona(PersonaDTO newPersonaRequest);

	PersonaDTO updatePersona(Long requestedId, PersonaDTO personaUpdate);

	void deletePersona(Long id);
}

package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import es.dsrroma.school.springboot.integracionbase.dtos.PersonaDTO;
import es.dsrroma.school.springboot.integracionbase.mappers.PersonaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Persona;
import es.dsrroma.school.springboot.integracionbase.repositories.PersonaRepository;

@Service
public class PersonaService {

	private final PersonaRepository personaRepository;

	public PersonaService(PersonaRepository personaRepository) {
		this.personaRepository = personaRepository;
	}

	public PersonaDTO findPersonaById(Long requestedId) {
		Optional<Persona> personaOpt = personaRepository.findById(requestedId);
		return personaOpt.map(PersonaMapper::toDTO).orElse(null);
	}

	public List<PersonaDTO> findAllPersonas() {
		Iterable<Persona> personas = personaRepository.findAll();
		return StreamSupport.stream(personas.spliterator(), false)
				.map(PersonaMapper::toDTO).collect(Collectors.toList());
	}

	public PersonaDTO createPersona(PersonaDTO newPersonaRequest) {
		Persona persona = PersonaMapper.toEntity(newPersonaRequest);
		Persona savedPersona = personaRepository.save(persona);
		return PersonaMapper.toDTO(savedPersona);
	}

	public PersonaDTO updatePersona(Long requestedId, PersonaDTO personaUpdate) {
		Optional<Persona> personaOpt = personaRepository.findById(requestedId);
		if (personaOpt.isPresent()) {
			Persona updatedPersona = personaOpt.get();
			updatedPersona.setNumeroEmpleado(personaUpdate.getNumeroEmpleado());
			updatedPersona.setNombre(personaUpdate.getNombre());
			updatedPersona.setApellidos(personaUpdate.getApellidos());
			Persona savedPersona = personaRepository.save(updatedPersona);
			return PersonaMapper.toDTO(savedPersona);
		}
		return null;
	}

	public boolean deletePersona(Long id) {
		if (personaRepository.existsById(id)) {
			personaRepository.deleteById(id);
			return true;
		}
		return false;
	}
}

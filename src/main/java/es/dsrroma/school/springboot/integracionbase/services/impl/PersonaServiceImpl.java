package es.dsrroma.school.springboot.integracionbase.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import es.dsrroma.school.springboot.integracionbase.dtos.PersonaDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;
import es.dsrroma.school.springboot.integracionbase.mappers.PersonaMapper;
import es.dsrroma.school.springboot.integracionbase.models.Persona;
import es.dsrroma.school.springboot.integracionbase.repositories.PersonaRepository;
import es.dsrroma.school.springboot.integracionbase.services.PersonaService;

@Service
public class PersonaServiceImpl implements PersonaService {

	private final PersonaRepository personaRepository;

	public PersonaServiceImpl(PersonaRepository personaRepository) {
		this.personaRepository = personaRepository;
	}

	@Override
	public PersonaDTO findPersonaById(Long requestedId) throws EntityNotFoundException {
		Persona persona = personaRepository.findById(requestedId)
				.orElseThrow(() -> new EntityNotFoundException("Persona", requestedId));
		return PersonaMapper.toDTO(persona);
	}

	@Override
	public List<PersonaDTO> findAllPersonas() {
		Iterable<Persona> personas = personaRepository.findAll();
		return StreamSupport.stream(personas.spliterator(), false)
				.map(PersonaMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public PersonaDTO createPersona(PersonaDTO newPersonaRequest) {
		Persona persona = PersonaMapper.toEntity(newPersonaRequest);
		Persona savedPersona = personaRepository.save(persona);
		return PersonaMapper.toDTO(savedPersona);
	}

	@Override
	public PersonaDTO updatePersona(Long requestedId, PersonaDTO personaUpdate) 
			throws EntityNotFoundException {
		Persona persona = personaRepository.findById(requestedId)
				.orElseThrow(() -> new EntityNotFoundException("Persona", requestedId));
		persona.setNumeroEmpleado(personaUpdate.getNumeroEmpleado());
		persona.setNombre(personaUpdate.getNombre());
		persona.setApellidos(personaUpdate.getApellidos());
		Persona savedPersona = personaRepository.save(persona);
		return PersonaMapper.toDTO(savedPersona);
	}

	@Override
    public void deletePersona(Long id) throws EntityNotFoundException {
        if (!personaRepository.existsById(id)) {
            throw new EntityNotFoundException("Persona", id);
        }
        personaRepository.deleteById(id);
    }
}

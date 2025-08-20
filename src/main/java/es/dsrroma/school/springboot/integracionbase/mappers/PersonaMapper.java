package es.dsrroma.school.springboot.integracionbase.mappers;

import es.dsrroma.school.springboot.integracionbase.dtos.PersonaDTO;
import es.dsrroma.school.springboot.integracionbase.models.Persona;

public class PersonaMapper {

	public static PersonaDTO toDTO(Persona entity) {
		if (entity == null) {
			return null;
		}
		PersonaDTO dto = new PersonaDTO();
		dto.setId(entity.getId());
		dto.setNumeroEmpleado(entity.getNumeroEmpleado());
		dto.setNombre(entity.getNombre());
		dto.setApellidos(entity.getApellidos());

		return dto;
	}

	public static Persona toEntity(PersonaDTO dto) {
		if (dto == null) {
			return null;
		}
		Persona entity = new Persona();
		entity.setId(dto.getId());
		entity.setNumeroEmpleado(dto.getNumeroEmpleado());
		entity.setNombre(dto.getNombre());
		entity.setApellidos(dto.getApellidos());
		return entity;
	}
}

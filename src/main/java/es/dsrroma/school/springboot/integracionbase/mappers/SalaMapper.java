package es.dsrroma.school.springboot.integracionbase.mappers;

import es.dsrroma.school.springboot.integracionbase.dtos.SalaDTO;
import es.dsrroma.school.springboot.integracionbase.models.Sala;

public class SalaMapper {

	public static SalaDTO toDTO(Sala entity) {
		if (entity == null) {
			return null;
		}
		SalaDTO dto = new SalaDTO();
		dto.setId(entity.getId());
		dto.setDescripcion(entity.getDescripcion());
		dto.setCapacidad(entity.getCapacidad());
		return dto;
	}

	public static Sala toEntity(SalaDTO dto) {
		if (dto == null) {
			return null;
		}
		Sala entity = new Sala();
		entity.setId(dto.getId());
		entity.setDescripcion(dto.getDescripcion());
		entity.setCapacidad(dto.getCapacidad());
		return entity;
	}
}

package es.dsrroma.school.springboot.integracionbase.mappers;

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.models.Acta;

public class ActaMapper {

	public static ActaDTO toDTO(Acta entity) {
		if (entity == null) {
			return null;
		}
		ActaDTO dto = new ActaDTO();
		dto.setId(entity.getId());
		dto.setContenido(entity.getContenido());
		return dto;
	}

	public static Acta toEntity(ActaDTO dto) {
		if (dto == null) {
			return null;
		}
		Acta entity = new Acta();
		entity.setId(dto.getId());
		entity.setContenido(dto.getContenido());
		return entity;
	}
}

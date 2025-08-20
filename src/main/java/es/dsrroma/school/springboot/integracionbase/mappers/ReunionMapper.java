package es.dsrroma.school.springboot.integracionbase.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import es.dsrroma.school.springboot.integracionbase.dtos.ReunionDTO;
import es.dsrroma.school.springboot.integracionbase.models.Persona;
import es.dsrroma.school.springboot.integracionbase.models.Reunion;

public class ReunionMapper {

	public static ReunionDTO toDTO(Reunion entity) {
		if (entity == null) {
			return null;
		}
		ReunionDTO dto = new ReunionDTO();
		dto.setId(entity.getId());
		dto.setAsunto(entity.getAsunto());
		dto.setFecha(entity.getFecha());
		dto.setActaId(entity.getActa() != null ? entity.getActa().getId() : null);
		dto.setSalaId(entity.getSala() != null ? entity.getSala().getId() : null);

		if (entity.getParticipantes() != null && !entity.getParticipantes().isEmpty()) {
			Set<Long> participantesIds = entity.getParticipantes().stream().map(Persona::getId)
					.collect(Collectors.toSet());
			dto.setParticipantesIds(participantesIds);
		}

		return dto;
	}

	public static Reunion toEntity(ReunionDTO dto) {
		if (dto == null) {
			return null;
		}
		Reunion entity = new Reunion();
		entity.setId(dto.getId());
		entity.setAsunto(dto.getAsunto());
		entity.setFecha(dto.getFecha());

		// Acta, Sala y Participantes se cargan en servicio
//		entity.setActa(dto.getActaId() != null ? new Acta(dto.getActaId(), null) : null);
//		entity.setSala(dto.getSalaId() != null ? new Sala(dto.getSalaId(), null, 0) : null);

		return entity;
	}
}

package es.dsrroma.school.springboot.integracionbase.dtos;

import java.time.Instant;
import java.util.Set;

import jakarta.validation.constraints.Future;
import lombok.Data;

@Data
public class ReunionDTO {
	private Long id;
	private String asunto;
	@Future(message = "La fecha de la reunión debe ser futura")
	private Instant fecha;
	private Long actaId;
	private String salaId;
	private Set<Long> participantesIds;
}

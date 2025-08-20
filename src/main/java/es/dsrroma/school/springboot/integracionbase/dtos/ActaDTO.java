package es.dsrroma.school.springboot.integracionbase.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ActaDTO {
	private Long id;

	@NotBlank(message = "El contenido del acta no puede estar vacío")
	@Size(min = 30, message = "El contenido del acta no puede ser de menos de 30 caracteres")
	@Size(max = 1023, message = "El contenido del acta no puede ser de m�s de 1023 caracteres")
	private String contenido;
}

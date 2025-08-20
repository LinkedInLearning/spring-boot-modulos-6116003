package es.dsrroma.school.springboot.integracionbase.dtos;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SalaDTO {
	private String id;
	private String descripcion;
	@Positive(message = "La capacidad de la sala debe ser un valor positivo")
	private int capacidad;
}

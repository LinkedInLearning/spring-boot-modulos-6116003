package es.dsrroma.school.springboot.integracionbase.dtos;

import lombok.Data;

@Data
public class PersonaDTO {
	private Long id;
	private String numeroEmpleado;
	private String nombre;
	private String apellidos;
}

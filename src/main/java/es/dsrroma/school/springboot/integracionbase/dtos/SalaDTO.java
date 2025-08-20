package es.dsrroma.school.springboot.integracionbase.dtos;

import lombok.Data;

@Data
public class SalaDTO {
	private String id;
	private String descripcion;
	private int capacidad;
}

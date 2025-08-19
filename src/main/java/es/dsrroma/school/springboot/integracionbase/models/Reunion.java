package es.dsrroma.school.springboot.integracionbase.models;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "reuniones")
@Data
@AllArgsConstructor
@NoArgsConstructor // sin este todo compila, pero hibernate no funciona
public class Reunion {

	@Id
	private String id; // MongoDB usa Strings como ID
	private String asunto;
	private Instant fecha;

}

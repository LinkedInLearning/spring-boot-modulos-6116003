package es.dsrroma.school.springboot.integracionbase.models;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor // sin este todo compila, pero hibernate no funciona
public class Reunion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@NonNull
	private Long id;
	@NonNull
	private String asunto;
	@NonNull
	private Instant fecha;

	@ManyToOne
	@JoinColumn
	private Sala sala;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(referencedColumnName = "id")
	private Acta acta;

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Persona> participantes;
}

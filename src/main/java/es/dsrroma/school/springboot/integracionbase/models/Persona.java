package es.dsrroma.school.springboot.integracionbase.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@NonNull
	private Long id;

	@Column(unique = true)
	@NonNull
	@EqualsAndHashCode.Include
	private String numeroEmpleado;
	@NonNull
	private String nombre;
	@NonNull
	private String apellidos;

	@ManyToMany(mappedBy = "participantes", cascade = CascadeType.REMOVE) 
	// cascade para poder borrar las reuniones con participantes
	private Set<Reunion> reuniones = new HashSet<Reunion>();
}

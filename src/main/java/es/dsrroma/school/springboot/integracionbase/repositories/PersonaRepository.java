package es.dsrroma.school.springboot.integracionbase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.dsrroma.school.springboot.integracionbase.models.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
	@Query("SELECT p FROM Reunion r JOIN r.participantes p WHERE r.acta.contenido LIKE %:palabra%")
	List<Persona> encontrarParticipantesDeReunionesConPalabraEnActa(@Param("palabra") String palabra);
}

package es.dsrroma.school.springboot.integracionbase.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.dsrroma.school.springboot.integracionbase.models.Reunion;

public interface ReunionRepository extends JpaRepository<Reunion, Long> {
	@Query("SELECT r FROM Reunion r WHERE SIZE(r.participantes) = :n")
	List<Reunion> encontrarReunionesConNParticipantes(@Param("n") int nun);
	
	@Query("SELECT r FROM Reunion r WHERE r.sala.capacidad > :capacidad")
	List<Reunion> encontrarReunionesConSalaCapacidadMayorQue(@Param("capacidad") int capacidad);
	
	@Query("SELECT r FROM Reunion r JOIN r.participantes p WHERE p.apellidos = :apellido")
	List<Reunion> encontrarReunionesPorParticipanteApellido(@Param("apellido") String apellido);

	/** Reuniones anteriores a una fecha sin acta */
	List<Reunion> findByFechaBeforeAndActaIsNull(Instant fecha);
}

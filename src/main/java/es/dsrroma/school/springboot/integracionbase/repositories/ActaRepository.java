package es.dsrroma.school.springboot.integracionbase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.dsrroma.school.springboot.integracionbase.models.Acta;

public interface ActaRepository extends JpaRepository<Acta, Long> {
	@Query("SELECT r.acta FROM Reunion r JOIN r.participantes p WHERE p.id = :personaId")
	List<Acta> findActasPorParticipante(@Param("personaId") Long personaId);
}

package es.dsrroma.school.springboot.integracionbase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.dsrroma.school.springboot.integracionbase.models.Acta;

public interface ActaRepository extends JpaRepository<Acta, Long> {
	@Query("SELECT r.acta FROM Reunion r JOIN r.participantes p WHERE p.id = :personaId")
	List<Acta> findActasPorParticipante(@Param("personaId") Long personaId);
	
	@Query(value = "SELECT a.* FROM acta a " + "JOIN reunion r ON r.acta_id = a.id "
			+ "JOIN reunion_participantes rp ON r.id = rp.reuniones_id "
			+ "JOIN persona p ON rp.participantes_id = p.id " + "WHERE p.id = :personaId "
			+ "AND DATE_FORMAT(r.fecha, '%Y-%m') = :fechaMes", nativeQuery = true)
	List<Acta> findActasPorParticipanteYMes(@Param("personaId") Long personaId, 
			@Param("fechaMes") String fechaMes);

	// Versión ORACLE
//	@Query(value = "SELECT a.* FROM acta a " + "JOIN reunion r ON r.acta_id = a.id "
//			+ "JOIN reunion_participantes rp ON r.id = rp.reuniones_id "
//			+ "JOIN persona p ON rp.participantes_id = p.id " + "WHERE p.id = :personaId "
//			+ "AND TO_CHAR(r.fecha, 'YYYY-MM') = :fechaMes", nativeQuery = true)
//	List<Acta> findActasPorParticipanteYMes(@Param("personaId") Long personaId, 
//			@Param("fechaMes") String fechaMes);
}

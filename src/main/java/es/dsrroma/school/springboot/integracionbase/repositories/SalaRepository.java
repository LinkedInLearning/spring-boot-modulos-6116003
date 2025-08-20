package es.dsrroma.school.springboot.integracionbase.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.dsrroma.school.springboot.integracionbase.models.Sala;

public interface SalaRepository extends JpaRepository<Sala, String> {
	Set<Sala> findByCapacidadGreaterThanEqual(int num);
	Set<Sala> findLast3ByCapacidadBetween(int min, int max);
	
	@Query("select s from Sala s where s.id like 'A%'")
	Set<Sala> encontrarAulas();
	
	@Query(value = "select * from sala where id like 'A%'", nativeQuery = true)
	Set<Sala> encontrarAulasNativo();
	
	@Query("SELECT r.sala FROM Reunion r JOIN r.participantes p WHERE p.nombre = :nombre")
	List<Sala> encontrarSalasPorParticipante(@Param("nombre") String nombre);
}

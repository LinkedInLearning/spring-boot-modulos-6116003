package es.dsrroma.school.springboot.integracionbase.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dsrroma.school.springboot.integracionbase.models.Sala;

public interface SalaRepository extends JpaRepository<Sala, String> {
	Set<Sala> findByCapacidadGreaterThanEqual(int num);
	Set<Sala> findLast3ByCapacidadBetween(int min, int max);
}

package es.dsrroma.school.springboot.integracionbase.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import es.dsrroma.school.springboot.integracionbase.models.Sala;

public interface SalaRepository extends CrudRepository<Sala, String> {
	Set<Sala> findByCapacidadGreaterThanEqual(int num);
}

package es.dsrroma.school.springboot.integracionbase.repositories;

import org.springframework.data.repository.CrudRepository;

import es.dsrroma.school.springboot.integracionbase.models.Sala;

public interface SalaRepository extends CrudRepository<Sala, String> {

}

package es.dsrroma.school.springboot.integracionbase.repositories;

import org.springframework.data.repository.CrudRepository;

import es.dsrroma.school.springboot.integracionbase.models.Persona;

public interface PersonaRepository extends CrudRepository<Persona, Long> {

}

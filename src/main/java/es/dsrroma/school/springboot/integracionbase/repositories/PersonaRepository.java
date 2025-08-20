package es.dsrroma.school.springboot.integracionbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dsrroma.school.springboot.integracionbase.models.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

}

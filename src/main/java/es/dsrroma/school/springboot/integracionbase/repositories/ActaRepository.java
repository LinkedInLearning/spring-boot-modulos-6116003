package es.dsrroma.school.springboot.integracionbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dsrroma.school.springboot.integracionbase.models.Acta;

public interface ActaRepository extends JpaRepository<Acta, Long> {

}

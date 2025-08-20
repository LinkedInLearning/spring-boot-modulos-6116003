package es.dsrroma.school.springboot.integracionbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dsrroma.school.springboot.integracionbase.models.Reunion;

public interface ReunionRepository extends JpaRepository<Reunion, Long> {

}

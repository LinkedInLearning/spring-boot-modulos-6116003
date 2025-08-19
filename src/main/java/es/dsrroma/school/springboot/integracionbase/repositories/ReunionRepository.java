package es.dsrroma.school.springboot.integracionbase.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import es.dsrroma.school.springboot.integracionbase.models.Reunion;

public interface ReunionRepository extends CrudRepository<Reunion, Long>, PagingAndSortingRepository<Reunion, Long> {

}

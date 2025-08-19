package es.dsrroma.school.springboot.integracionbase.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import es.dsrroma.school.springboot.integracionbase.models.Reunion;

@Repository
public interface ReunionRepository extends MongoRepository<Reunion, Long> {

}

package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.dtos.ReunionDTO;
import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;

public interface ReunionService {

	ReunionDTO findReunionById(Long requestedId) throws EntityNotFoundException;

	List<ReunionDTO> findAllReuniones();

	List<ReunionDTO> findAllReuniones(Pageable pageable);

	List<ReunionDTO> findAllReuniones2(Pageable pageable);

	ReunionDTO createReunion(ReunionDTO newReunionRequest) throws EntityNotFoundException;

	ReunionDTO updateReunion(Long requestedId, ReunionDTO reunionUpdate) throws EntityNotFoundException;

	void deleteReunion(Long id) throws EntityNotFoundException;

	void addSalaToReunion(Long reunionId, String salaId) throws EntityNotFoundException;

	void addActaToReunion(Long reunionId, ActaDTO actaRequest) throws EntityNotFoundException;

	void addParticipantes(Long reunionId, Set<Long> participantesIds) throws EntityNotFoundException;
}

package es.dsrroma.school.springboot.integracionbase.services;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;

import es.dsrroma.school.springboot.integracionbase.dtos.ActaDTO;
import es.dsrroma.school.springboot.integracionbase.dtos.ReunionDTO;

public interface ReunionService {

	ReunionDTO findReunionById(Long requestedId);

	List<ReunionDTO> findAllReuniones();

	List<ReunionDTO> findAllReuniones(Pageable pageable);

	List<ReunionDTO> findAllReuniones2(Pageable pageable);

	ReunionDTO createReunion(ReunionDTO newReunionRequest);

	ReunionDTO updateReunion(Long requestedId, ReunionDTO reunionUpdate);

	void deleteReunion(Long id);

	void addSalaToReunion(Long reunionId, String salaId);

	void addActaToReunion(Long reunionId, ActaDTO actaRequest);

	void addParticipantes(Long reunionId, Set<Long> participantesIds);
}

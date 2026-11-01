package kigali.gym.rw.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.SessionStatus;
import kigali.gym.rw.entity.TrainingSession;
import java.util.List;
import kigali.gym.rw.entity.SessionStatus;
import java.time.LocalDate;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, UUID> {

    
    boolean existsByTrainer_IdAndSessionDateAndStatusNotAndIdNot(UUID trainerId, LocalDate sessionDate, SessionStatus status,
  UUID id);

   
    boolean existsByMember_Id(UUID memberId);

    boolean existsByTrainer_Id(UUID trainerId);
   // B2
    List<TrainingSession> findByStatusOrderBySessionDateAsc(SessionStatus status);
    // B3 
    List<TrainingSession> findBySessionDateBetweenOrderBySessionDateAsc(
            LocalDate start,
            LocalDate end
    );
}
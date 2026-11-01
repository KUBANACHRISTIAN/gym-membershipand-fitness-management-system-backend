package kigali.gym.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.SessionStatus;
import kigali.gym.rw.entity.TrainingSession;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, UUID> {

    boolean existsByTrainer_IdAndSessionDateAndStatusNotAndIdNot(
            UUID trainerId,
            LocalDate sessionDate,
            SessionStatus status,
            UUID id
    );

    boolean existsByMember_Id(UUID memberId);

    boolean existsByTrainer_Id(UUID trainerId);

    // B2 
    List<TrainingSession> findByStatusOrderBySessionDateAsc(SessionStatus status);

    // B3 
    List<TrainingSession> findBySessionDateBetweenOrderBySessionDateAsc(
            LocalDate start,
            LocalDate end
    );

    // D1 
    boolean existsByTrainer_IdAndSessionDateAndStatusNot(
            UUID trainerId,
            LocalDate sessionDate,
            SessionStatus status
    );

    // bonus 
    long countBySessionDate(LocalDate sessionDate);

    long countByStatus(SessionStatus status);

    // F1 
    @Query("""
    SELECT s.status, COUNT(s)
    FROM TrainingSession s
    GROUP BY s.status
    """)
    List<Object[]> countSessionsByStatus();

    // F2
    @Modifying(clearAutomatically = true)
    @Query("""
    UPDATE TrainingSession s
    SET s.status = kigali.gym.rw.entity.SessionStatus.CANCELLED
    WHERE s.trainer.id = :trainerId
      AND s.sessionDate = :date
      AND s.status <> kigali.gym.rw.entity.SessionStatus.FINISHED
    """)
    int cancelDayForTrainer(@Param("trainerId") UUID trainerId,
     @Param("date") LocalDate date);
}
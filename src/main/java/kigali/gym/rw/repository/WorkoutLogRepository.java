package kigali.gym.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.TrainingSession;
import kigali.gym.rw.entity.WorkoutLog;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, UUID> {

    // E2 (JPQL): newest first
    @Query("""
    SELECT d
    FROM WorkoutLog d
    WHERE d.trainingSession.member.membershipNumber = :membershipNumber
    ORDER BY d.loggedDate DESC
    """)
    List<WorkoutLog> findLogsOfMember(@Param("membershipNumber") String membershipNumber);

    // E3 (DERIVED)
    boolean existsByTrainingSession(TrainingSession trainingSession);
}

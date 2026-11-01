package kigali.gym.rw.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.WorkoutLog;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, UUID> {
}
package kigali.gym.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.TrainingSpecialty;

@Repository
public interface TrainingSpecialtyRepository extends JpaRepository<TrainingSpecialty, UUID> {

    
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);
 // C3
    @Query("""
    SELECT s
    FROM TrainingSpecialty s
    WHERE s.trainers IS EMPTY
    """)
    List<TrainingSpecialty> findUnusedSpecialties();
}
package kigali.gym.rw.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.TrainingSpecialty;

@Repository
public interface TrainingSpecialtyRepository extends JpaRepository<TrainingSpecialty, UUID> {

    
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);
}
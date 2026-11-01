package kigali.gym.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.Studio;
import kigali.gym.rw.entity.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, UUID> {

    
    boolean existsByStudio(Studio studio);


    boolean existsByStudioAndIdNot(Studio studio, UUID id);

 //C1
    @Query("""
    SELECT DISTINCT t
    FROM Trainer t
    JOIN t.specialties s
    WHERE LOWER(s.name) = LOWER(:name)
    """)
    List<Trainer> findTrainersBySpecialtyName(@Param("name") String name);
}
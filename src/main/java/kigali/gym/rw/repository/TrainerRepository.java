package kigali.gym.rw.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.Studio; 
import kigali.gym.rw.entity.Trainer; 


@Repository
public interface TrainerRepository extends JpaRepository<Trainer, UUID> {

    
    boolean existsByStudio(Studio studio);


    boolean existsByStudioAndIdNot(Studio studio, UUID id);
}
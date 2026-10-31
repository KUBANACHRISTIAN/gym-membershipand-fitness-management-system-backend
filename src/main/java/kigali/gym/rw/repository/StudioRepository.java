package kigali.gym.rw.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.Studio;

@Repository
public interface StudioRepository extends JpaRepository<Studio, UUID> {

    Optional<Studio> findByStudioNumber(int studioNumber);

    boolean existsByStudioNumber(int studioNumber);

    boolean existsByStudioNumberAndIdNot(int studioNumber, UUID id);
}
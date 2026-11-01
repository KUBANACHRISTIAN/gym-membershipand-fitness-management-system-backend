package kigali.gym.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.Studio;
@Repository
public interface StudioRepository extends JpaRepository<Studio, UUID> {

    Optional<Studio> findByStudioNumber(int studioNumber);

    boolean existsByStudioNumber(int studioNumber);

    boolean existsByStudioNumberAndIdNot(int studioNumber, UUID id);

    // C2
    @Query("""
    SELECT l
    FROM Studio l
    WHERE NOT EXISTS (
        SELECT p FROM Trainer p WHERE p.studio = l
    )
    ORDER BY l.studioNumber
    """)
    List<Studio> findFreeStudios();
}
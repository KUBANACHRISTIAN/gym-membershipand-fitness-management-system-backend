package kigali.gym.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.Member;
@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByMembershipNumber(String membershipNumber);

 // B1 
 
    List<Member> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);


    // C4
    @Query("""
    SELECT DISTINCT s.member
    FROM TrainingSession s
    WHERE s.trainer.id = :trainerId
    """)
    List<Member> findMembersOfTrainer(@Param("trainerId") UUID trainerId);

}
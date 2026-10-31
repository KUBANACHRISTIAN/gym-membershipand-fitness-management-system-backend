package kigali.gym.rw.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.gym.rw.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByMembershipNumber(String membershipNumber);
}
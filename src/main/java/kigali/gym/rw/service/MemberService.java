package kigali.gym.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.gym.rw.entity.Member;
import kigali.gym.rw.repository.MemberRepository;
import kigali.gym.rw.repository.TrainingSessionRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private TrainingSessionRepository sessionRepo;

    // A2
    public String saveMember(Member member) {

        if (member.getMembershipNumber() == null || member.getMembershipNumber().isBlank()) {
            return "membershipNumber is required";
        }

    
        if (memberRepo.findByMembershipNumber(member.getMembershipNumber()).isPresent()) {
            return "A member with that membershipNumber already exists";
        }

        member.setId(null);
        memberRepo.save(member);

        return "Member saved successfully";
    }

    public List<Member> getAllMembers() {
        return memberRepo.findAll();
    }

    public Optional<Member> getOneMember(UUID id) {
        return memberRepo.findById(id);
    }

    public String updateMember(UUID id, Member member) {

        Optional<Member> existing = memberRepo.findById(id);

        if (existing.isEmpty()) {
            return "The member with that id does not exist";
        }

        Member memberToUpdate = existing.get();

        if (member.getMembershipNumber() != null && !member.getMembershipNumber().isBlank()) {

            Optional<Member> sameNumber = memberRepo.findByMembershipNumber(member.getMembershipNumber());

            if (sameNumber.isPresent() && !sameNumber.get().getId().equals(id)) {
                return "A member with that membershipNumber already exists";
            }

            memberToUpdate.setMembershipNumber(member.getMembershipNumber());
        }

        memberToUpdate.setFirstName(member.getFirstName());
        memberToUpdate.setLastName(member.getLastName());
        memberToUpdate.setEmail(member.getEmail());
        memberToUpdate.setPhone(member.getPhone());

        memberRepo.save(memberToUpdate);

        return "Member updated successfully";
    }

    public String deleteMember(UUID id) {

        if (!memberRepo.existsById(id)) {
            return "The member with that id does not exist";
        }

        if (sessionRepo.existsByMember_Id(id)) {
            return "That member has training sessions, delete them first";
        }

        memberRepo.deleteById(id);

        return "Member deleted successfully";
    }
}
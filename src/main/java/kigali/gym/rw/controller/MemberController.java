package kigali.gym.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.gym.rw.entity.Member;
import kigali.gym.rw.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/save")
    public ResponseEntity<?> saveMember(@RequestBody Member member) {

        String message = memberService.saveMember(member);

        if (message.contains("is required")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if (message.contains("already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMembers() {

        List<Member> members = memberService.getAllMembers();

        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneMember(@PathVariable UUID id) {

        Optional<Member> member = memberService.getOneMember(id);

        if (member.isPresent()) {
            return new ResponseEntity<>(member.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>("The member with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMember(@PathVariable UUID id, @RequestBody Member member) {

        String message = memberService.updateMember(id, member);

        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.contains("already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable UUID id) {

        String message = memberService.deleteMember(id);

        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.contains("delete them first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    // B1
    @GetMapping("/by-last-name")
    public ResponseEntity<?> getMembersByLastName(@RequestParam String lastName) {

        List<Member> members = memberService.getMembersByLastName(lastName);

        return new ResponseEntity<>(members, HttpStatus.OK);
    }
     // C4
    @GetMapping("/of-trainer/{trainerId}")
    public ResponseEntity<?> getMembersOfTrainer(@PathVariable("trainerId") UUID trainerId) {

        Optional<List<Member>> members = memberService.getMembersOfTrainer(trainerId);

        if (members.isEmpty()) {
            return new ResponseEntity<>(
                    "The trainer with that id does not exist",
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(members.get(), HttpStatus.OK);
    }
}
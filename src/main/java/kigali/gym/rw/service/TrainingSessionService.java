package kigali.gym.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.gym.rw.entity.Member;
import kigali.gym.rw.entity.SessionStatus;
import kigali.gym.rw.entity.Trainer;
import kigali.gym.rw.entity.TrainingSession;
import kigali.gym.rw.repository.MemberRepository;
import kigali.gym.rw.repository.TrainerRepository;
import kigali.gym.rw.repository.TrainingSessionRepository;

@Service
public class TrainingSessionService {

    @Autowired
    private TrainingSessionRepository sessionRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private TrainerRepository trainerRepo;

    // A2 

    public String saveSession(TrainingSession session) {

        Optional<Member> member = findMember(session.getMember());

        if (member.isEmpty()) {
            return "The member with that id does not exist";
        }

        Optional<Trainer> trainer = findTrainer(session.getTrainer());

        if (trainer.isEmpty()) {
            return "The trainer with that id does not exist";
        }

        session.setId(null);
        session.setMember(member.get());
        session.setTrainer(trainer.get());

       
        if (session.getStatus() == null) {
            session.setStatus(SessionStatus.BOOKED);
        }

        sessionRepo.save(session);

        return "Training session saved successfully";
    }

    public List<TrainingSession> getAllSessions() {
        return sessionRepo.findAll();
    }

    public Optional<TrainingSession> getOneSession(UUID id) {
        return sessionRepo.findById(id);
    }

    
    public String updateSession(UUID id, TrainingSession body) {

        Optional<TrainingSession> existing = sessionRepo.findById(id);

        if (existing.isEmpty()) {
            return "The training session with that id does not exist";
        }

        TrainingSession session = existing.get();

        Member member = session.getMember();
        if (body.getMember() != null) {
            Optional<Member> found = findMember(body.getMember());
            if (found.isEmpty()) {
                return "The member with that id does not exist";
            }
            member = found.get();
        }

        Trainer trainer = session.getTrainer();
        if (body.getTrainer() != null) {
            Optional<Trainer> found = findTrainer(body.getTrainer());
            if (found.isEmpty()) {
                return "The trainer with that id does not exist";
            }
            trainer = found.get();
        }

        LocalDate date = body.getSessionDate() != null ? body.getSessionDate() : session.getSessionDate();

        if (date == null) {
            return "Training session date is required";
        }

        boolean moved = !Objects.equals(date, session.getSessionDate())
                || !trainer.getId().equals(session.getTrainer().getId());

        
                
        if (moved) {
            if (date.isBefore(LocalDate.now())) {
                return "Training session date cannot be in the past";
            }

            if (sessionRepo.existsByTrainer_IdAndSessionDateAndStatusNotAndIdNot(
                    trainer.getId(), date, SessionStatus.CANCELLED, id)) {
                return "Trainer already has a session on that date";
            }
        }

        session.setMember(member);
        session.setTrainer(trainer);
        session.setSessionDate(date);

        if (body.getDurationMinutes() > 0) {
            session.setDurationMinutes(body.getDurationMinutes());
        }

       
        if (body.getStatus() != null) {
            session.setStatus(body.getStatus());
        }

        sessionRepo.save(session);

        return "Training session updated successfully";
    }

    public String deleteSession(UUID id) {

        if (!sessionRepo.existsById(id)) {
            return "The training session with that id does not exist";
        }

        sessionRepo.deleteById(id);

        return "Training session deleted successfully";
    }

    private Optional<Member> findMember(Member fromBody) {
        if (fromBody == null || fromBody.getId() == null) {
            return Optional.empty();
        }
        return memberRepo.findById(fromBody.getId());
    }

    private Optional<Trainer> findTrainer(Trainer fromBody) {
        if (fromBody == null || fromBody.getId() == null) {
            return Optional.empty();
        }
        return trainerRepo.findById(fromBody.getId());
    }
}
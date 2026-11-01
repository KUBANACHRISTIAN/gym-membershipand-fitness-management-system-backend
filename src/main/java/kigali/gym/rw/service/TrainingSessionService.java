package kigali.gym.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.Optional;
import java.util.UUID;

import kigali.gym.rw.entity.SessionStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.gym.rw.entity.Member;
import kigali.gym.rw.entity.SessionStatus;
import kigali.gym.rw.entity.Trainer;
import kigali.gym.rw.entity.TrainingSession;
import kigali.gym.rw.repository.MemberRepository;
import kigali.gym.rw.repository.TrainerRepository;
import kigali.gym.rw.repository.TrainingSessionRepository;
import java.time.LocalDate;
import kigali.gym.rw.repository.WorkoutLogRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TrainingSessionService {

    @Autowired
    private TrainingSessionRepository sessionRepo;
    @Autowired
    private WorkoutLogRepository workoutLogRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private TrainerRepository trainerRepo;

       // A2 + D1
    public String saveSession(TrainingSession session) {

        if (session.getMember() == null || session.getMember().getId() == null) {
            return "The member with that id does not exist";
        }

        Optional<Member> member = memberRepo.findById(session.getMember().getId());

        if (member.isEmpty()) {
            return "The member with that id does not exist";
        }

        if (session.getTrainer() == null || session.getTrainer().getId() == null) {
            return "The trainer with that id does not exist";
        }

        Optional<Trainer> trainer = trainerRepo.findById(session.getTrainer().getId());

        if (trainer.isEmpty()) {
            return "The trainer with that id does not exist";
        }

        if (session.getSessionDate() == null) {
            return "Training session date is required";
        }

        if (session.getSessionDate().isBefore(LocalDate.now())) {
            return "Training session date cannot be in the past";
        }

        if (sessionRepo.existsByTrainer_IdAndSessionDateAndStatusNot(
                trainer.get().getId(),
                session.getSessionDate(),
                SessionStatus.CANCELLED)) {
            return "Trainer already has a session on that date";
        }

        session.setMember(member.get());
        session.setTrainer(trainer.get());

        session.setStatus(SessionStatus.BOOKED);

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

      // A2 + E3
    public String deleteSession(UUID id) {

        Optional<TrainingSession> session = sessionRepo.findById(id);

        if (session.isEmpty()) {
            return "The training session with that id does not exist";
        }

        if (workoutLogRepo.existsByTrainingSession(session.get())) {
            return "That training session has workout logs, delete them first";
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
    // B2
    public List<TrainingSession> getSessionsByStatus(SessionStatus status) {
        return sessionRepo.findByStatusOrderBySessionDateAsc(status);
    }
      // B3
    public List<TrainingSession> getSessionsBetween(LocalDate start, LocalDate end) {
        return sessionRepo.findBySessionDateBetweenOrderBySessionDateAsc(start, end);
    }
        // D2
    public String updateStatus(UUID id, SessionStatus newStatus) {

        Optional<TrainingSession> session = sessionRepo.findById(id);

        if (session.isEmpty()) {
            return "The training session with that id does not exist";
        }

        SessionStatus current = session.get().getStatus();
        boolean allowed = false;

        if (current == SessionStatus.BOOKED
                && (newStatus == SessionStatus.CHECKED_IN || newStatus == SessionStatus.CANCELLED)) {
            allowed = true;
        }

        if (current == SessionStatus.CHECKED_IN
                && (newStatus == SessionStatus.FINISHED || newStatus == SessionStatus.CANCELLED)) {
            allowed = true;
        }

       

        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }

        session.get().setStatus(newStatus);
        sessionRepo.save(session.get());

        return "Training session status updated successfully";
    }
        // F1
    public Map<String, Long> countSessionsByStatus() {

        Map<String, Long> res = new LinkedHashMap<>();

        for (Object[] row : sessionRepo.countSessionsByStatus()) {
            res.put(row[0].toString(), (Long) row[1]);
        }

        return res;
    }
    // F2
    @Transactional
    public String cancelDay(UUID trainerId, LocalDate date) {

        int res = sessionRepo.cancelDayForTrainer(trainerId, date);

        return res + " training sessions cancelled";
    }
     // bonus
    public Page<TrainingSession> getSessionsPage(Pageable pageable) {
        return sessionRepo.findAll(pageable);
    }
}
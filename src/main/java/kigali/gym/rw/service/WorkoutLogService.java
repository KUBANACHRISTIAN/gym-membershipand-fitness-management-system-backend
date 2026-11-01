package kigali.gym.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.gym.rw.entity.SessionStatus;
import kigali.gym.rw.entity.TrainingSession;
import kigali.gym.rw.entity.WorkoutLog;
import kigali.gym.rw.repository.TrainingSessionRepository;
import kigali.gym.rw.repository.WorkoutLogRepository;

@Service
public class WorkoutLogService {

    @Autowired
    private WorkoutLogRepository workoutLogRepo;

    @Autowired
    private TrainingSessionRepository sessionRepo;

    // E1
    public String saveWorkoutLog(UUID trainingSessionId, WorkoutLog log) {

        Optional<TrainingSession> session = sessionRepo.findById(trainingSessionId);

        if (session.isEmpty()) {
            return "The training session with that id does not exist";
        }

        if (session.get().getStatus() != SessionStatus.FINISHED) {
            return "Workout logs are only for FINISHED training sessions";
        }

        log.setLoggedDate(LocalDate.now());
        log.setTrainingSession(session.get());

        workoutLogRepo.save(log);

        return "Workout log saved successfully";
    }

    public List<WorkoutLog> getAllWorkoutLogs() {
        return workoutLogRepo.findAll();
    }
     // E2
    public List<WorkoutLog> getLogsOfMember(String membershipNumber) {
        return workoutLogRepo.findLogsOfMember(membershipNumber);
    }
       // E3
    public String deleteWorkoutLog(UUID id) {

        if (!workoutLogRepo.existsById(id)) {
            return "The workout log with that id does not exist";
        }

        workoutLogRepo.deleteById(id);

        return "Workout log deleted successfully";
    }
}
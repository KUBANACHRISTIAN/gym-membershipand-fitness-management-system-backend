package kigali.gym.rw.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.gym.rw.entity.SessionStatus;
import kigali.gym.rw.repository.MemberRepository;
import kigali.gym.rw.repository.StudioRepository;
import kigali.gym.rw.repository.TrainerRepository;
import kigali.gym.rw.repository.TrainingSessionRepository;
import kigali.gym.rw.repository.WorkoutLogRepository;

@Service
public class DashboardService {

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private TrainerRepository trainerRepo;

    @Autowired
    private StudioRepository studioRepo;

    @Autowired
    private TrainingSessionRepository sessionRepo;

    @Autowired
    private WorkoutLogRepository workoutLogRepo;

    public Map<String, Long> getSummary() {

        // LinkedHashMap so the JSON keeps this order
        Map<String, Long> res = new LinkedHashMap<>();

        res.put("members", memberRepo.count());
        res.put("trainers", trainerRepo.count());
        res.put("freeStudios", (long) studioRepo.findFreeStudios().size());
        res.put("sessionsToday", sessionRepo.countBySessionDate(LocalDate.now()));
        res.put("booked", sessionRepo.countByStatus(SessionStatus.BOOKED));
        res.put("workoutLogs", workoutLogRepo.count());

        return res;
    }
}

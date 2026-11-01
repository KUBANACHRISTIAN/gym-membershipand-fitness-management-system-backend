package kigali.gym.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.gym.rw.entity.WorkoutLog;
import kigali.gym.rw.service.WorkoutLogService;

@RestController
@RequestMapping("/api/workout-logs")
public class WorkoutLogController {

    @Autowired
    private WorkoutLogService workoutLogService;

    // E1
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveWorkoutLog(
            @RequestParam("trainingSessionId") UUID trainingSessionId,
            @RequestBody WorkoutLog log) {

        String message = workoutLogService.saveWorkoutLog(trainingSessionId, log);

        if (message.equals("The training session with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.equals("Workout logs are only for FINISHED training sessions")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllWorkoutLogs() {

        List<WorkoutLog> logs = workoutLogService.getAllWorkoutLogs();

        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
     // E2
    @GetMapping("/member/{membershipNumber}")
    public ResponseEntity<?> getLogsOfMember(@PathVariable("membershipNumber") String membershipNumber) {

        List<WorkoutLog> logs = workoutLogService.getLogsOfMember(membershipNumber);

        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
        // E3
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkoutLog(@PathVariable("id") UUID id) {

        String message = workoutLogService.deleteWorkoutLog(id);

        if (message.equals("The workout log with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
package kigali.gym.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
 import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import kigali.gym.rw.entity.SessionStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import kigali.gym.rw.entity.TrainingSession;
import kigali.gym.rw.service.TrainingSessionService;
import kigali.gym.rw.entity.SessionStatus;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/api/training-sessions")
public class TrainingSessionController {

    @Autowired
    private TrainingSessionService sessionService;

      // A2 + D1
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveSession(@RequestBody TrainingSession session) {

        String message = sessionService.saveSession(session);

        if (message.equals("The member with that id does not exist")
                || message.equals("The trainer with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.equals("Training session date cannot be in the past")
                || message.equals("Training session date is required")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if (message.equals("Trainer already has a session on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllSessions() {

        List<TrainingSession> sessions = sessionService.getAllSessions();

        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneSession(@PathVariable UUID id) {

        Optional<TrainingSession> session = sessionService.getOneSession(id);

        if (session.isPresent()) {
            return new ResponseEntity<>(session.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(
                "The training session with that id does not exist",
                HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSession(@PathVariable UUID id,
        @RequestBody TrainingSession session) {


        String message = sessionService.updateSession(id, session);

        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.equals("Training session date cannot be in the past")
                || message.equals("Training session date is required")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if (message.equals("Trainer already has a session on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

     // A2 + E3
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable UUID id) {

        String message = sessionService.deleteSession(id);

        if (message.equals("The training session with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.equals("That training session has workout logs, delete them first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    // B2
    @GetMapping("/by-status")
    public ResponseEntity<?> getSessionsByStatus(@RequestParam SessionStatus status) {

        List<TrainingSession> sessions = sessionService.getSessionsByStatus(status);

        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }
    // B3
    @GetMapping("/between")
    public ResponseEntity<?> getSessionsBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        List<TrainingSession> sessions = sessionService.getSessionsBetween(start, end);

        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }
        // D2
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable("id") UUID id,
            @RequestParam("status") SessionStatus status) {

        String message = sessionService.updateStatus(id, status);

        if (message.equals("The training session with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.startsWith("Cannot change status")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
        // F1
    @GetMapping("/stats/by-status")
    public ResponseEntity<?> countSessionsByStatus() {

        Map<String, Long> result = sessionService.countSessionsByStatus();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
        // F2
    @PatchMapping("/cancel-day")
    public ResponseEntity<?> cancelDay(
            @RequestParam("trainerId") UUID trainerId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String message = sessionService.cancelDay(trainerId, date);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
        // bonus
    @GetMapping("/page")
    public ResponseEntity<?> getSessionsPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort", defaultValue = "sessionDate,asc") String sort) {

        if (page < 0 || size < 1) {
            return new ResponseEntity<>("page must be 0 or more and size at least 1", HttpStatus.BAD_REQUEST);
        }

        String[] parts = sort.split(",");
        String field = parts[0].trim();

        if (!field.equals("sessionDate") && !field.equals("durationMinutes") && !field.equals("status")) {
            return new ResponseEntity<>("Cannot sort by " + field, HttpStatus.BAD_REQUEST);
        }

        Sort.Direction dir = Sort.Direction.ASC;
        if (parts.length > 1 && parts[1].trim().equalsIgnoreCase("desc")) {
            dir = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, field));

        Page<TrainingSession> res = sessionService.getSessionsPage(pageable);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

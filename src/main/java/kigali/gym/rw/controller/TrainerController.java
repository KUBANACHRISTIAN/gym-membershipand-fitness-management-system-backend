package kigali.gym.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.gym.rw.entity.Trainer;
import kigali.gym.rw.service.TrainerService;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @PostMapping("/save")
    public ResponseEntity<?> saveTrainer(@RequestBody Trainer trainer) {

        String message = trainerService.saveTrainer(trainer);

        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.contains("is taken")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTrainers() {

        List<Trainer> trainers = trainerService.getAllTrainers();

        return new ResponseEntity<>(trainers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneTrainer(@PathVariable UUID id) {

        Optional<Trainer> trainer = trainerService.getOneTrainer(id);

        if (trainer.isPresent()) {
            return new ResponseEntity<>(trainer.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>("The trainer with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTrainer(@PathVariable UUID id, @RequestBody Trainer trainer) {

        String message = trainerService.updateTrainer(id, trainer);

        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.contains("is taken")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTrainer(@PathVariable UUID id) {

        String message = trainerService.deleteTrainer(id);

        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.contains("delete them first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
     // C1

    @GetMapping("/by-training-specialty")
    public ResponseEntity<?> getTrainersBySpecialty(@RequestParam("name") String name) {

        List<Trainer> trainers = trainerService.getTrainersBySpecialty(name);

        return new ResponseEntity<>(trainers, HttpStatus.OK);
    }
        // D3
    @PutMapping("/{trainerId}/training-specialties/{trainingSpecialtyId}")
    public ResponseEntity<?> addSpecialty(
            @PathVariable("trainerId") UUID trainerId,
            @PathVariable("trainingSpecialtyId") UUID trainingSpecialtyId) {

        String message = trainerService.addSpecialty(trainerId, trainingSpecialtyId);

        if (message.equals("The trainer with that id does not exist")
                || message.equals("That training specialty does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.equals("The trainer already has that training specialty")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
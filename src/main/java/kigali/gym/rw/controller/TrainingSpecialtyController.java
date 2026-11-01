package kigali.gym.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.gym.rw.entity.TrainingSpecialty;
import kigali.gym.rw.service.TrainingSpecialtyService;

@RestController
@RequestMapping("/api/training-specialties")
public class TrainingSpecialtyController {

    @Autowired
    private TrainingSpecialtyService specialtyService;

    @PostMapping("/save")
    public ResponseEntity<?> saveSpecialty(@RequestBody TrainingSpecialty specialty) {

        String message = specialtyService.saveSpecialty(specialty);

        if (message.contains("is required")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if (message.contains("already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllSpecialties() {

        List<TrainingSpecialty> specialties = specialtyService.getAllSpecialties();

        return new ResponseEntity<>(specialties, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneSpecialty(@PathVariable UUID id) {

        Optional<TrainingSpecialty> specialty = specialtyService.getOneSpecialty(id);

        if (specialty.isPresent()) {
            return new ResponseEntity<>(specialty.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(
                "The training specialty with that id does not exist",
                HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSpecialty(@PathVariable UUID id,
                                             @RequestBody TrainingSpecialty specialty) {

        String message = specialtyService.updateSpecialty(id, specialty);

        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.contains("is required")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if (message.contains("already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSpecialty(@PathVariable UUID id) {

        String message = specialtyService.deleteSpecialty(id);

        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
     // C3
    @GetMapping("/unused")
    public ResponseEntity<?> getUnusedSpecialties() {

        List<TrainingSpecialty> specialties = specialtyService.getUnusedSpecialties();

        return new ResponseEntity<>(specialties, HttpStatus.OK);
    }
}
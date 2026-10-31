package kigali.gym.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.gym.rw.entity.Studio;
import kigali.gym.rw.service.StudioService;

@RestController
@RequestMapping("/api/studios")
public class StudioController {

    @Autowired
    private StudioService studioService;

    @PostMapping("/save")
    public ResponseEntity<?> saveStudio(@RequestBody Studio studio) {

        String message = studioService.saveStudio(studio);

        if (message.contains("is required")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if (message.contains("already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllStudios() {

        List<Studio> studios = studioService.getAllStudios();

        return new ResponseEntity<>(studios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneStudio(@PathVariable UUID id) {

        Optional<Studio> studio = studioService.getOneStudio(id);

        if (studio.isPresent()) {
            return new ResponseEntity<>(studio.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>("The studio with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudio(@PathVariable UUID id, @RequestBody Studio studio) {

        String message = studioService.updateStudio(id, studio);

        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.contains("already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudio(@PathVariable UUID id) {

        String message = studioService.deleteStudio(id);

        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.contains("is taken")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
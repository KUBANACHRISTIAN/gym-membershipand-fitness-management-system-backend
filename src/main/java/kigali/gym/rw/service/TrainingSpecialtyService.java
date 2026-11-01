package kigali.gym.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.gym.rw.entity.Trainer;
import kigali.gym.rw.entity.TrainingSpecialty;
import kigali.gym.rw.repository.TrainingSpecialtyRepository;

@Service
public class TrainingSpecialtyService {

    @Autowired
    private TrainingSpecialtyRepository specialtyRepo;

    // A2
    public String saveSpecialty(TrainingSpecialty specialty) {

        if (specialty.getName() == null || specialty.getName().isBlank()) {
            return "Training specialty name is required";
        }

        if (specialtyRepo.existsByNameIgnoreCase(specialty.getName().trim())) {
            return "A training specialty with that name already exists";
        }

        specialty.setId(null);
        specialtyRepo.save(specialty);

        return "Training specialty saved successfully";
    }

    public List<TrainingSpecialty> getAllSpecialties() {
        return specialtyRepo.findAll();
    }

    public Optional<TrainingSpecialty> getOneSpecialty(UUID id) {
        return specialtyRepo.findById(id);
    }

    public String updateSpecialty(UUID id, TrainingSpecialty specialty) {

        Optional<TrainingSpecialty> existing = specialtyRepo.findById(id);

        if (existing.isEmpty()) {
            return "The training specialty with that id does not exist";
        }

        if (specialty.getName() == null || specialty.getName().isBlank()) {
            return "Training specialty name is required";
        }

        if (specialtyRepo.existsByNameIgnoreCaseAndIdNot(specialty.getName().trim(), id)) {
            return "A training specialty with that name already exists";
        }

        existing.get().setName(specialty.getName());
        specialtyRepo.save(existing.get());

        return "Training specialty updated successfully";
    }

    @Transactional
    public String deleteSpecialty(UUID id) {

        Optional<TrainingSpecialty> specialty = specialtyRepo.findById(id);

        if (specialty.isEmpty()) {
            return "The training specialty with that id does not exist";
        }

        
        for (Trainer trainer : specialty.get().getTrainers()) {
            trainer.getSpecialties().remove(specialty.get());
        }

        specialtyRepo.delete(specialty.get());

        return "Training specialty deleted successfully";
    }
       
    // C3
    public List<TrainingSpecialty> getUnusedSpecialties() {
        return specialtyRepo.findUnusedSpecialties();
    }
}
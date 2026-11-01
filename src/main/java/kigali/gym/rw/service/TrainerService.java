package kigali.gym.rw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.gym.rw.entity.Studio;
import kigali.gym.rw.entity.Trainer;
import kigali.gym.rw.entity.TrainingSpecialty;
import kigali.gym.rw.repository.StudioRepository;
import kigali.gym.rw.repository.TrainerRepository;
import kigali.gym.rw.repository.TrainingSessionRepository;
import kigali.gym.rw.repository.TrainingSpecialtyRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainerService {

    @Autowired
    private TrainerRepository trainerRepo;

    @Autowired
    private StudioRepository studioRepo;

    @Autowired
    private TrainingSpecialtyRepository specialtyRepo;

    @Autowired
    private TrainingSessionRepository sessionRepo;

    // A2
   
    public String saveTrainer(Trainer trainer) {

        if (trainer.getStudio() != null) {

            Optional<Studio> studio = findStudio(trainer.getStudio());

            if (studio.isEmpty()) {
                return "The studio with that studioNumber does not exist";
            }

            if (trainerRepo.existsByStudio(studio.get())) {
                return "That studio is taken by another trainer";
            }

            trainer.setStudio(studio.get());
        }

        List<TrainingSpecialty> specialties = new ArrayList<>();

        if (trainer.getSpecialties() != null) {
            for (TrainingSpecialty s : trainer.getSpecialties()) {

                Optional<TrainingSpecialty> found =
                        s.getId() == null ? Optional.empty() : specialtyRepo.findById(s.getId());

                if (found.isEmpty()) {
                    return "That training specialty does not exist";
                }

                if (!specialties.contains(found.get())) {
                    specialties.add(found.get());
                }
            }
        }

        trainer.setSpecialties(specialties);
        trainer.setId(null);
        trainerRepo.save(trainer);

        return "Trainer saved successfully";
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepo.findAll();
    }

    public Optional<Trainer> getOneTrainer(UUID id) {
        return trainerRepo.findById(id);
    }

    public String updateTrainer(UUID id, Trainer trainer) {

        Optional<Trainer> existing = trainerRepo.findById(id);

        if (existing.isEmpty()) {
            return "The trainer with that id does not exist";
        }

        Trainer trainerToUpdate = existing.get();

        trainerToUpdate.setFirstName(trainer.getFirstName());
        trainerToUpdate.setLastName(trainer.getLastName());
        trainerToUpdate.setPhone(trainer.getPhone());

        
        if (trainer.getStudio() != null) {

            Optional<Studio> studio = findStudio(trainer.getStudio());

            if (studio.isEmpty()) {
                return "The studio with that studioNumber does not exist";
            }

            if (trainerRepo.existsByStudioAndIdNot(studio.get(), id)) {
                return "That studio is taken by another trainer";
            }

            trainerToUpdate.setStudio(studio.get());
        }

        if (trainer.getSpecialties() != null) {

            List<TrainingSpecialty> specialties = new ArrayList<>();

            for (TrainingSpecialty s : trainer.getSpecialties()) {
                Optional<TrainingSpecialty> found =
                        s.getId() == null ? Optional.empty() : specialtyRepo.findById(s.getId());

                if (found.isEmpty()) {
                    return "That training specialty does not exist";
                }

                if (!specialties.contains(found.get())) {
                    specialties.add(found.get());
                }
            }

            trainerToUpdate.getSpecialties().clear();
            trainerToUpdate.getSpecialties().addAll(specialties);
        }

        trainerRepo.save(trainerToUpdate);

        return "Trainer updated successfully";
    }

    public String deleteTrainer(UUID id) {

        if (!trainerRepo.existsById(id)) {
            return "The trainer with that id does not exist";
        }

        if (sessionRepo.existsByTrainer_Id(id)) {
            return "That trainer has training sessions, delete them first";
        }

        
        trainerRepo.deleteById(id);

        return "Trainer deleted successfully";
    }

    
    private Optional<Studio> findStudio(Studio fromBody) {
        if (fromBody.getId() != null) {
            return studioRepo.findById(fromBody.getId());
        }
        return studioRepo.findByStudioNumber(fromBody.getStudioNumber());
    }
    // C1
    public List<Trainer> getTrainersBySpecialty(String name) {
        return trainerRepo.findTrainersBySpecialtyName(name);
    }
        // D3
    @Transactional
    public String addSpecialty(UUID trainerId, UUID specialtyId) {

        Optional<Trainer> trainer = trainerRepo.findById(trainerId);

        if (trainer.isEmpty()) {
            return "The trainer with that id does not exist";
        }

        Optional<TrainingSpecialty> specialty = specialtyRepo.findById(specialtyId);

        if (specialty.isEmpty()) {
            return "That training specialty does not exist";
        }

        if (trainer.get().getSpecialties().contains(specialty.get())) {
            return "The trainer already has that training specialty";
        }

        trainer.get().getSpecialties().add(specialty.get());
        trainerRepo.save(trainer.get());

        return "Training specialty added to the trainer";
    }
}
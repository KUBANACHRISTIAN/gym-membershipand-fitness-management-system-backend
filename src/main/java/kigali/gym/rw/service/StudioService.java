package kigali.gym.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.gym.rw.entity.Studio;
import kigali.gym.rw.repository.StudioRepository;
import kigali.gym.rw.repository.TrainerRepository;

@Service
public class StudioService {

    @Autowired
    private StudioRepository studioRepo;

    @Autowired
    private TrainerRepository trainerRepo;

    // A2
    public String saveStudio(Studio studio) {

        
        if (studio.getStudioNumber() <= 0) {
            return "Studio number is required and must be greater than 0";
        }

        if (studioRepo.existsByStudioNumber(studio.getStudioNumber())) {
            return "A studio with that studioNumber already exists";
        }

        studio.setId(null);
        studioRepo.save(studio);

        return "Studio saved successfully";
    }

    public List<Studio> getAllStudios() {
        return studioRepo.findAll();
    }

    public Optional<Studio> getOneStudio(UUID id) {
        return studioRepo.findById(id);
    }

    public String updateStudio(UUID id, Studio studio) {

        Optional<Studio> existing = studioRepo.findById(id);

        if (existing.isEmpty()) {
            return "The studio with that id does not exist";
        }

        Studio studioToUpdate = existing.get();

        if (studio.getStudioNumber() > 0) {
            if (studioRepo.existsByStudioNumberAndIdNot(studio.getStudioNumber(), id)) {
                return "A studio with that studioNumber already exists";
            }
            studioToUpdate.setStudioNumber(studio.getStudioNumber());
        }

        if (studio.getName() != null) {
            studioToUpdate.setName(studio.getName());
        }

        studioRepo.save(studioToUpdate);

        return "Studio updated successfully";
    }

    public String deleteStudio(UUID id) {

        Optional<Studio> studio = studioRepo.findById(id);

        if (studio.isEmpty()) {
            return "The studio with that id does not exist";
        }

        if (trainerRepo.existsByStudio(studio.get())) {
            return "That studio is taken by a trainer, move the trainer first";
        }

        studioRepo.deleteById(id);

        return "Studio deleted successfully";
    }
}
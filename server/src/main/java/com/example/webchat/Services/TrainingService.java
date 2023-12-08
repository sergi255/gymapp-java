package com.example.webchat.Services;

import com.example.webchat.DTOs.ExerciseDTO;
import com.example.webchat.Models.Exercise;
import com.example.webchat.Models.Training;
import com.example.webchat.Repositories.TrainingRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    public Optional<Training> getTrainingByName(String name) {
        return trainingRepository.findTrainingByName(name);
    }

    public Optional<Training> getTrainingById(ObjectId id) {
        return trainingRepository.findById((ObjectId) id);
    }

    public Training createTraining(String name, String description, String date, List<Exercise> exercises) {
        if (name.isEmpty() || description.isEmpty() || date.isEmpty() || exercises.isEmpty()) {
            throw new IllegalArgumentException("Training name, description, and exercises cannot be empty.");
        }
        Optional<Training> existingTrainingWithName = trainingRepository.findTrainingByName(name);
        if (existingTrainingWithName.isPresent()) {
            throw new IllegalArgumentException("Training with this name already exists.");
        }
        Training training = new Training(name, description, date, exercises);
        return trainingRepository.save(training);
    }

    public Training updateTraining(String id, String name, String description, String date, List<Exercise> exercises) {
        Optional<Training> existingTraining = trainingRepository.findById(new ObjectId(id));
        if (existingTraining.isPresent()) {
            Training updatedTraining = existingTraining.get();
            updatedTraining.updateTraining(name, description, date, exercises);

            return trainingRepository.save(updatedTraining);
        } else {
            throw new IllegalArgumentException("Training with the given id does not exist.");
        }
    }
    public void deleteTraining(String id) {
        Optional<Training> existingTraining = trainingRepository.findById(new ObjectId(id));
        if (existingTraining.isPresent()) {
            trainingRepository.delete(existingTraining.get());
        } else {
            throw new IllegalArgumentException("Training with the given id does not exist.");
        }
    }

}

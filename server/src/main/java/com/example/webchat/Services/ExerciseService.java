package com.example.webchat.Services;

import com.example.webchat.Models.Exercise;
import com.example.webchat.Repositories.ExerciseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;
    public List<Exercise> getAllExercises(){
        return exerciseRepository.findAll();
    }
    public List<Exercise> searchExercisesByExerciseName(String name) {
        return exerciseRepository.findByNameContainingIgnoreCase(name);
    }
    public Optional<Exercise> getExerciseById(ObjectId id) {
        return exerciseRepository.findById(id);
    }

    public Exercise createExercise(String name, String description, String category) {
        if (name.isEmpty() || description.isEmpty() || category.isEmpty()) {
            throw new IllegalArgumentException("Exercise name, description, and category cannot be empty.");
        }
        Optional<Exercise> existingExerciseWithExerciseName = exerciseRepository.findByName(name);
        if (existingExerciseWithExerciseName.isPresent()) {
            throw new IllegalArgumentException("Exercise with this name already exists.");
        }
        Exercise exercise = exerciseRepository.insert(new Exercise(name, description, category));
        return exercise;
    }

    public void updateExerciseById(String id, String name, String description, String category) {
        Optional<Exercise> existingExercise = exerciseRepository.findById(id);
        if (existingExercise.isPresent()) {
            Exercise exerciseToUpdate = existingExercise.get();
            exerciseToUpdate.setName(name);
            exerciseToUpdate.setDescription(description);
            exerciseToUpdate.setCategory(category);
            exerciseRepository.save(exerciseToUpdate);
        } else {
            throw new IllegalArgumentException("Exercise with this id does not exist.");
        }
    }

    public void deleteExerciseById(String id) {
        Optional<Exercise> existingExerciseWithExerciseName = exerciseRepository.findById(id);
        if (existingExerciseWithExerciseName.isPresent()) {
            exerciseRepository.delete(existingExerciseWithExerciseName.get());
        }
        else{
            throw new IllegalArgumentException("Exercise with this id does not exist.");
        }
    }
    public List<Exercise> getExercisesByNames(List<Exercise> exercises) {
        return exerciseRepository.findByNameIn(exercises);
    }

}

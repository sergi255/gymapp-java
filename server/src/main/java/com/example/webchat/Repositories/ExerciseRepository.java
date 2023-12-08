package com.example.webchat.Repositories;

import com.example.webchat.Models.Exercise;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
public interface ExerciseRepository extends MongoRepository<Exercise, ObjectId> {
    Optional<Exercise> findByName(String name);
    List<Exercise> findByNameContainingIgnoreCase(String name);
    List<Exercise> findByNameIn(List<Exercise> exercises);
    Optional<Exercise> findById(String id);
}


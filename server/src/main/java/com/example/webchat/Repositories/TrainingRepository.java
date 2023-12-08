package com.example.webchat.Repositories;

import com.example.webchat.Models.Training;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TrainingRepository extends MongoRepository<Training, ObjectId> {
    Optional<Training> findTrainingByName(String name);
}

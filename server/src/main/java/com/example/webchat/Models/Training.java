package com.example.webchat.Models;

import com.example.webchat.DTOs.ExerciseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "trainings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    @Id
    private ObjectId id;
    private String name;
    private String description;
    private String date;
    private List<Exercise> exercises;

    public Training(String name, String description, String date, List<Exercise> exercises) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.exercises = exercises;
    }
    //UpdateTrainining Model added
    public void updateTraining(String name, String description, String date, List<Exercise> exercises) {
        if (name != null) {
            this.name = name;
        }

        if (description != null) {
            this.description = description;
        }

        if (date != null) {
            this.date = date;
        }

        if (exercises != null) {
            this.exercises = exercises;
        }
    }
}

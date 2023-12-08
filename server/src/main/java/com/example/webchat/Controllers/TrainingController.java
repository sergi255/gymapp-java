package com.example.webchat.Controllers;

import com.example.webchat.DTOs.ExerciseDTO;
import com.example.webchat.DTOs.TrainingDTO;
import com.example.webchat.Models.Exercise;
import com.example.webchat.Models.Training;
import com.example.webchat.Services.ExerciseService;
import com.example.webchat.Services.TrainingService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private UserController userController;
    @Autowired
    private ExerciseService exerciseService;


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getAllTrainings")
    public ResponseEntity<List<Training>> getAllTrainings(@RequestBody TrainingDTO trainingRequest) {
        if(trainingRequest.getToken().equals(userController.getToken())) {
            return new ResponseEntity<>(trainingService.getAllTrainings(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getTrainingById/{id}")
    public ResponseEntity<Training> getTrainingById(@PathVariable String id, @RequestBody TrainingDTO trainingRequest) {
        if (trainingRequest.getToken().equals(userController.getToken())) {
            try {
                ObjectId objectId = new ObjectId(id);
                Optional<Training> optionalTraining = trainingService.getTrainingById(objectId);
                return optionalTraining
                        .map(training -> new ResponseEntity<>(training, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public ResponseEntity<Training> createTraining(@RequestBody TrainingDTO trainingRequest) {
        if (trainingRequest.getToken().equals(userController.getToken())) {
            try {
                return new ResponseEntity<>(trainingService.createTraining(trainingRequest.getName(), trainingRequest.getDescription(), trainingRequest.getDate(), trainingRequest.getExercises()), HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    //Update Training Endpoint Added
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/updateTraining/{id}")
    public ResponseEntity<Training> updateTraining(@PathVariable String id, @RequestBody TrainingDTO trainingRequest) {
        if (trainingRequest.getToken().equals(userController.getToken())) {
            try {
                return new ResponseEntity<>(trainingService.updateTraining(id, trainingRequest.getName(), trainingRequest.getDescription(), trainingRequest.getDate(), trainingRequest.getExercises()), HttpStatus.OK);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    //Delete Training Endpoint Added
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/deleteTraining/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable String id, @RequestBody TrainingDTO trainingRequest) {
        if (trainingRequest.getToken().equals(userController.getToken())) {
            try {
                trainingService.deleteTraining(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}

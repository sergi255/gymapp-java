package com.example.webchat.Controllers;

import com.example.webchat.Models.Exercise;
import com.example.webchat.DTOs.ExerciseDTO;
import com.example.webchat.Models.User;
import com.example.webchat.Repositories.ExerciseRepository;
import com.example.webchat.Services.ExerciseService;


import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private UserController userController;
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getAllExercises")
    public ResponseEntity<List<Exercise>> getAllExercises(@RequestBody ExerciseDTO exerciseRequest) {
        if (exerciseRequest.getToken().equals(userController.getToken())) {
            return new ResponseEntity<>(exerciseService.getAllExercises(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/find/{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable String id, @RequestBody ExerciseDTO exerciseRequest) {
        if (exerciseRequest.getToken().equals(userController.getToken())) {
            try {
                ObjectId objectId = new ObjectId(id);
                Optional<Exercise> optionalExercise = exerciseService.getExerciseById(objectId);

                return optionalExercise
                        .map(exercise -> new ResponseEntity<>(exercise, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Exercise>> searchExercises(@PathVariable String name, @RequestBody ExerciseDTO exerciseRequest) {
        if (exerciseRequest.getToken().equals(userController.getToken())) {
            List<Exercise> matchingExercises = exerciseService.searchExercisesByExerciseName(name);
            return new ResponseEntity<>(matchingExercises, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public ResponseEntity<Exercise> createExercise(@RequestBody ExerciseDTO exerciseRequest) {
        if (exerciseRequest.getToken().equals(userController.getToken())) {
            try {
                return new ResponseEntity<>(exerciseService.createExercise(exerciseRequest.getName(), exerciseRequest.getDescription(), exerciseRequest.getCategory()), HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateExerciseById(@PathVariable String id, @RequestBody ExerciseDTO exerciseRequest) {
        if(exerciseRequest.getToken().equals(userController.getToken())) {
            try {
                exerciseService.updateExerciseById(id, exerciseRequest.getName(), exerciseRequest.getDescription(), exerciseRequest.getCategory());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch(Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Exercise> deleteExerciseById(@PathVariable String id, @RequestBody ExerciseDTO exerciseRequest) {
        if(exerciseRequest.getToken().equals(userController.getToken())) {
            try {
                exerciseService.deleteExerciseById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch(Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
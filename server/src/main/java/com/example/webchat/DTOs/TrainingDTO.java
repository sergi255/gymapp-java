package com.example.webchat.DTOs;

import com.example.webchat.Models.Exercise;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDTO {
    private String name;
    private String description;
    private String date;
    private List<Exercise> exercises;
    private String token;
    public String getToken() {
        return token;
    }
}
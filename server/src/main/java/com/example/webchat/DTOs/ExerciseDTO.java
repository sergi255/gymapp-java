package com.example.webchat.DTOs;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDTO {
    private String name;
    private String description;
    private String category;
    private String token;

    public String getToken() {
        return token;
    }
}
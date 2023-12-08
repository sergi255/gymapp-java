package com.example.webchat.Models;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "exercises")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String name;
    private String description;
    private String category;

    public Exercise(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }
}
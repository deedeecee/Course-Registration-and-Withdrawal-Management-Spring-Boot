package com.debankar.course_management_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
public class Course {
    @Id
    private ObjectId id;

    private String name;

    private String description;

    private int credits;

    private List<ObjectId> prerequisites; // List of course IDs required as prerequisites

    private int capacity; // Max number of students allowed enrolling a particular course

    private ObjectId instructorId; // ID of the instructor teaching this course
}

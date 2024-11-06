package com.debankar.course_management_system.dto;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
public class CourseCreationDTO {
    private String name;
    private String description;
    private int credits;
    private List<ObjectId> prerequisites; // List of prerequisite course ObjectIds
    private int capacity;
    private ObjectId instructorId; // ID of the instructor teaching this course
}

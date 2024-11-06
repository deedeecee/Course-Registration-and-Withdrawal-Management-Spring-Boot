package com.debankar.course_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CourseDTO {
    private String name;
    private String description;
    private int credits;
    private List<String> prerequisites; // List of prerequisite course names
    private int capacity;
    private String instructorName; // Instructor's name
}

package com.debankar.course_management_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "enrollments")
public class Enrollment {

    @Id
    private ObjectId id;

    private ObjectId studentId; // ID of the user with the role of STUDENT

    private ObjectId courseId; // ID of the enrolled course

    private LocalDate enrollmentDate;

    private EnrollmentStatus status; // Enum to track enrollment status (e.g., ACTIVE, WITHDRAWN)

    public enum EnrollmentStatus {
        ACTIVE,
        WITHDRAWN
    }
}
package com.debankar.course_management_system.repository;

import com.debankar.course_management_system.entity.Course;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, ObjectId> {
    List<Course> findByInstructorId(ObjectId instructorId);
}

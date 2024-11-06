package com.debankar.course_management_system.service;

import com.debankar.course_management_system.entity.Course;
import com.debankar.course_management_system.repository.CourseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Create a new course
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    // Get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get course by ID
    public Optional<Course> getCourseById(ObjectId id) {
        return courseRepository.findById(id);
    }

    // Update an existing course
    public Course updateCourse(ObjectId courseId, Course courseDetails) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();

            if (courseDetails.getName() != null) {
                existingCourse.setName(courseDetails.getName());
            }

            if (courseDetails.getDescription() != null) {
                existingCourse.setDescription(courseDetails.getDescription());
            }

            if (courseDetails.getCredits() != 0) {
                existingCourse.setCredits(courseDetails.getCredits());
            }

            if (courseDetails.getCapacity() != 0) {
                existingCourse.setCapacity(courseDetails.getCapacity());
            }

            if (courseDetails.getPrerequisites() != null) {
                existingCourse.setPrerequisites(courseDetails.getPrerequisites());
            }

            if (courseDetails.getInstructorId() != null) {
                existingCourse.setInstructorId(courseDetails.getInstructorId());
            }

            return courseRepository.save(existingCourse);
        }

        throw new RuntimeException("Course not found with id: " + courseId);
    }

    public List<Course> getCoursesByInstructorId(ObjectId instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    // Delete course by ID
    public void deleteCourse(ObjectId id) {
        courseRepository.deleteById(id);
    }
}
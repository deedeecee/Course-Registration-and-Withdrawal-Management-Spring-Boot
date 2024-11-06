package com.debankar.course_management_system.service;

import com.debankar.course_management_system.entity.Enrollment;
import com.debankar.course_management_system.entity.User;
import com.debankar.course_management_system.repository.EnrollmentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    // Create a new enrollment
    public Enrollment createEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    // Get all enrollments
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    // Get enrollment by ID
    public Optional<Enrollment> getEnrollmentById(ObjectId id) {
        return enrollmentRepository.findById(id);
    }

    // Get enrollments by Student ID
    public List<Enrollment> getEnrollmentsByStudentId(ObjectId studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    // Delete enrollment by ID
    public void deleteEnrollment(ObjectId id) {
        enrollmentRepository.deleteById(id);
    }

    // Get a specific enrollment using Student ID and Course ID
    public Optional<Enrollment> findEnrollmentByStudentIdAndCourseId(ObjectId studentId, ObjectId courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    // Update enrollment
    public void updateEnrollment(Enrollment enrollment) {
        enrollmentRepository.save(enrollment);
    }

    public Optional<List<Enrollment>> getEnrollmentsByCourseIdAndStatus(ObjectId courseId,
                                                                        Enrollment.EnrollmentStatus status) {
        return enrollmentRepository.findByCourseIdAndStatus(courseId, status);
    }
}
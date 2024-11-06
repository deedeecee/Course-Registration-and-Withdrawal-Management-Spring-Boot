package com.debankar.course_management_system.repository;

import com.debankar.course_management_system.entity.Enrollment;
import com.debankar.course_management_system.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends MongoRepository<Enrollment, ObjectId> {
    List<Enrollment> findByStudentId(ObjectId studentId);
    Optional<Enrollment> findByStudentIdAndCourseId(ObjectId studentId, ObjectId courseId);
    Optional<List<Enrollment>> findByCourseIdAndStatus(ObjectId courseId, Enrollment.EnrollmentStatus status);

}

package com.debankar.course_management_system.repository;

import com.debankar.course_management_system.entity.Withdrawal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WithdrawalRepository extends MongoRepository<Withdrawal, ObjectId> {
    void deleteByStudentIdAndCourseId(ObjectId studentId, ObjectId courseId);
}

package com.debankar.course_management_system.service;

import com.debankar.course_management_system.entity.Withdrawal;
import com.debankar.course_management_system.repository.WithdrawalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;

    @Autowired
    public WithdrawalService(WithdrawalRepository withdrawalRepository) {
        this.withdrawalRepository = withdrawalRepository;
    }

    // Create a new withdrawal
    public Withdrawal createWithdrawal(Withdrawal withdrawal) {
        return withdrawalRepository.save(withdrawal);
    }

    // Get all withdrawals
    public List<Withdrawal> getAllWithdrawals() {
        return withdrawalRepository.findAll();
    }

    // Get withdrawal by ID
    public Optional<Withdrawal> getWithdrawalById(ObjectId id) {
        return withdrawalRepository.findById(id);
    }

    // Delete withdrawal by ID
    public void deleteWithdrawal(ObjectId id) {
        withdrawalRepository.deleteById(id);
    }

    // Delete By Student ID and Course ID
    public void deleteByStudentIdAndCourseId(ObjectId studentId, ObjectId courseId) {
        withdrawalRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }
}
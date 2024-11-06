package com.debankar.course_management_system.controller;

import com.debankar.course_management_system.entity.Withdrawal;
import com.debankar.course_management_system.service.WithdrawalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/withdrawals")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    @Autowired
    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    // Create a new withdrawal
    @PostMapping
    public ResponseEntity<Withdrawal> createWithdrawal(@RequestBody Withdrawal withdrawal) {
        Withdrawal createdWithdrawal = withdrawalService.createWithdrawal(withdrawal);
        return new ResponseEntity<>(createdWithdrawal, HttpStatus.CREATED);
    }

    // Get all withdrawals
    @GetMapping
    public ResponseEntity<List<Withdrawal>> getAllWithdrawals() {
        List<Withdrawal> withdrawals = withdrawalService.getAllWithdrawals();
        return new ResponseEntity<>(withdrawals, HttpStatus.OK);
    }

    // Get withdrawal by ID
    @GetMapping("/{id}")
    public ResponseEntity<Withdrawal> getWithdrawalById(@PathVariable ObjectId id) {
        Optional<Withdrawal> withdrawal = withdrawalService.getWithdrawalById(id);
        return withdrawal.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete withdrawal by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWithdrawal(@PathVariable ObjectId id) {
        withdrawalService.deleteWithdrawal(id);
        return ResponseEntity.noContent().build();
    }
}

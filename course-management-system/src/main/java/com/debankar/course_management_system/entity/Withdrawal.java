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
@Document(collection = "withdrawals")
public class Withdrawal {

    @Id
    private ObjectId id;

    private ObjectId studentId; // ID of the user with the role of STUDENT

    private ObjectId courseId; // ID of the withdrawn course

    private LocalDate withdrawalDate;

//    private boolean penaltyApplied; // Indicates if a penalty was applied
//
//    private String penaltyReason; // Reason for the penalty, if applicable
}
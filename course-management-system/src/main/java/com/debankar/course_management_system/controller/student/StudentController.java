package com.debankar.course_management_system.controller.student;

import com.debankar.course_management_system.entity.Course;
import com.debankar.course_management_system.entity.Enrollment;
import com.debankar.course_management_system.entity.User;
import com.debankar.course_management_system.entity.Withdrawal;
import com.debankar.course_management_system.service.CourseService;
import com.debankar.course_management_system.service.EnrollmentService;
import com.debankar.course_management_system.service.UserService;
import com.debankar.course_management_system.service.WithdrawalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final CourseService courseService;
    private final WithdrawalService withdrawalService;

    @Autowired
    public StudentController(
            EnrollmentService enrollmentService,
            UserService userService,
            CourseService courseService,
            WithdrawalService withdrawalService) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.courseService = courseService;
        this.withdrawalService = withdrawalService;
    }

    // Create active enrollments using Student ID
    @PostMapping("/active/{studentId}")
    public ResponseEntity<List<Enrollment>> createActiveEnrollmentEntry(
            @PathVariable ObjectId studentId,
            @RequestParam List<ObjectId> activateCourses) {
        // Check if the user exists and has a STUDENT role
        Optional<User> optionalStudentUser = userService.getUserById(studentId);
        if (optionalStudentUser.isEmpty() || !(optionalStudentUser.get().getRole().equals(User.Role.STUDENT))) {
            throw new RuntimeException("Invalid Student ID!");
        }

        List<Enrollment> responseList = new ArrayList<>();

        for (ObjectId courseId : activateCourses) {
            Optional<Course> optionalCourse = courseService.getCourseById(courseId);

            if (optionalCourse.isPresent()) {
                // Check for an existing enrollment
                Optional<Enrollment> existingEnrollment =
                        enrollmentService.findEnrollmentByStudentIdAndCourseId(studentId, courseId);

                if (existingEnrollment.isPresent()) {
                    Enrollment enrollment = existingEnrollment.get();
                    if (enrollment.getStatus() == Enrollment.EnrollmentStatus.WITHDRAWN) {
                        // Reactivate the withdrawn enrollment
                        enrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);
                        enrollment.setEnrollmentDate(LocalDate.now());

                        // Save the updated enrollment
                        enrollmentService.updateEnrollment(enrollment);

                        // Remove the associated withdrawal entry
                        withdrawalService.deleteByStudentIdAndCourseId(studentId, courseId);

                        responseList.add(enrollment);
                    }
                } else {
                    // Create a new enrollment if no existing enrollment is found
                    Enrollment enrollment = new Enrollment();
                    enrollment.setStudentId(studentId);
                    enrollment.setCourseId(courseId);
                    enrollment.setEnrollmentDate(LocalDate.now());
                    enrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);

                    enrollmentService.createEnrollment(enrollment);
                    responseList.add(enrollment);
                }
            }
        }

        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    // Create withdrawn enrollments using Student ID
    @PostMapping("withdrawn/{studentId}")
    public ResponseEntity<List<Withdrawal>> createWithdrawnEnrollmentEntry(
            @PathVariable ObjectId studentId,
            @RequestParam List<ObjectId> withdrawnCourses) {
        Optional<User> optionalStudentUser = userService.getUserById(studentId);

        if ((optionalStudentUser.isEmpty()) || !(optionalStudentUser.get().getRole().equals(User.Role.STUDENT))) {
            throw new RuntimeException("Invalid Student ID!");
        }

        List<Withdrawal> responseList = new ArrayList<>();
        List<Enrollment> activeEnrollments = getActiveEnrollments(studentId).getBody();

        for (ObjectId courseId : withdrawnCourses) {
            assert activeEnrollments != null;
            for (Enrollment enrollment : activeEnrollments) {
                if (enrollment.getCourseId().equals(courseId)) {
                    enrollmentService.deleteEnrollment(enrollment.getId());
                    enrollment.setEnrollmentDate(LocalDate.now());
                    enrollment.setStatus(Enrollment.EnrollmentStatus.WITHDRAWN);
                    enrollmentService.createEnrollment(enrollment);

                    // Create a new Withdrawal Entry
                    Withdrawal withdrawal = new Withdrawal();
                    withdrawal.setStudentId(enrollment.getStudentId());
                    withdrawal.setCourseId(enrollment.getCourseId());
                    withdrawal.setWithdrawalDate(enrollment.getEnrollmentDate());
                    withdrawalService.createWithdrawal(withdrawal);

                    responseList.add(withdrawal);
                }
            }
        }

        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    // Get active enrollments using Student ID
    @GetMapping("active/{studentId}")
    public ResponseEntity<List<Enrollment>> getActiveEnrollments(@PathVariable ObjectId studentId) {
        Optional<User> optionalStudentUser = userService.getUserById(studentId);

        if ((optionalStudentUser.isEmpty()) || !(optionalStudentUser.get().getRole().equals(User.Role.STUDENT))) {
            throw new RuntimeException("Invalid Student ID!");
        }

        List<Enrollment> activeEnrollments =
                enrollmentService.getEnrollmentsByStudentId(studentId)
                        .stream()
                        .filter(enrollment -> enrollment.getStatus().equals(Enrollment.EnrollmentStatus.ACTIVE))
                        .collect(Collectors.toList());

        return new ResponseEntity<>(activeEnrollments, HttpStatus.OK);
    }

    // Get withdrawn enrollments using Student ID
    @GetMapping("withdrawn/{studentId}")
    public ResponseEntity<List<Enrollment>> getWithdrawnEnrollment(@PathVariable ObjectId studentId) {
        Optional<User> optionalStudentUser = userService.getUserById(studentId);

        if ((optionalStudentUser.isEmpty()) || !(optionalStudentUser.get().getRole().equals(User.Role.STUDENT))) {
            throw new RuntimeException("Invalid Student ID!");
        }

        List<Enrollment> withdrawnEnrollments =
                enrollmentService.getEnrollmentsByStudentId(studentId)
                        .stream()
                        .filter(enrollment -> enrollment.getStatus().equals(Enrollment.EnrollmentStatus.WITHDRAWN))
                        .collect(Collectors.toList());

        return new ResponseEntity<>(withdrawnEnrollments, HttpStatus.OK);
    }
}

package com.debankar.course_management_system.controller.admin;

import com.debankar.course_management_system.entity.Course;
import com.debankar.course_management_system.entity.User;
import com.debankar.course_management_system.service.CourseService;
import com.debankar.course_management_system.service.EnrollmentService;
import com.debankar.course_management_system.service.UserService;
import com.debankar.course_management_system.service.WithdrawalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final WithdrawalService withdrawalService;

    @Autowired
    public AdminController(
            CourseService courseService,
            EnrollmentService enrollmentService,
            UserService userService,
            WithdrawalService withdrawalService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.withdrawalService = withdrawalService;
    }

    // Create new user
    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Update existing user
    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable ObjectId userId, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(userId, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Delete existing user
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable ObjectId userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Get all users specific to a role or all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) User.Role role) {
        List<User> users = (role == null) ? userService.getAllUsers() : userService.getUsersByRole(role);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Create a course
    @PostMapping("/courses/{instructorId}")
    public ResponseEntity<Course> addCourse(
            @PathVariable ObjectId instructorId,
            @RequestBody Course course,
            @RequestParam(required = false) List<ObjectId> prerequisites) {
        course.setInstructorId(instructorId);
        course.setPrerequisites(prerequisites);
        Course createdCourse = courseService.createCourse(course);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    // Update a course
    @PutMapping("/courses/{courseId}")
    public ResponseEntity<Course> updateCourse(@PathVariable ObjectId courseId, @RequestBody Course courseDetails) {
        Course updatedCourse = courseService.updateCourse(courseId, courseDetails);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    // Get all courses
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    // Delete a course
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable ObjectId courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }
}

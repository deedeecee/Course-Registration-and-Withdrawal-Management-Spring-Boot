package com.debankar.course_management_system.controller;

import com.debankar.course_management_system.dto.CourseCreationDTO;
import com.debankar.course_management_system.dto.CourseDTO;
import com.debankar.course_management_system.dto.Mapper;
import com.debankar.course_management_system.entity.Course;
import com.debankar.course_management_system.entity.User;
import com.debankar.course_management_system.service.CourseService;
import com.debankar.course_management_system.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;
    private final Mapper mapper;

    @Autowired
    public CourseController(CourseService courseService, UserService userService, Mapper mapper) {
        this.courseService = courseService;
        this.userService = userService;
        this.mapper = mapper;
    }
    
    // create a new course with instructor id and prerequisites
    @PostMapping("/{instructorId}")
    public ResponseEntity<CourseDTO> createCourse(
            @PathVariable ObjectId instructorId,
            @RequestParam(required = false) List<ObjectId> prerequisiteIds,
            @RequestBody CourseCreationDTO courseDTO) {

        courseDTO.setInstructorId(instructorId);
        courseDTO.setPrerequisites(Objects.requireNonNullElseGet(prerequisiteIds, ArrayList::new));

        Course course = mapper.toCourse(courseDTO);

        courseService.createCourse(course);

        System.out.println("Created Course with Course ID: " + course.getId());

        List<String> prerequisiteCourses = new ArrayList<>();

        if (prerequisiteIds != null) {
            prerequisiteCourses = prerequisiteIds.stream().map(id -> courseService.getCourseById(id).get().getName()).collect(Collectors.toList());
        }

        return new ResponseEntity<>(mapper.toCourseDTO(course, userService.getUserById(instructorId).get().getName(),
                prerequisiteCourses), HttpStatus.CREATED);
    }

    // get all courses
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();

        List<CourseDTO> courseDTOs = courses.stream().map(course -> {
            // Retrieve instructor's name and prerequisite names for the DTO
            String instructorName = userService.getUserById(course.getInstructorId())
                    .map(User::getName).orElse("Unknown Instructor");

            List<String> prerequisiteNames = course.getPrerequisites().stream()
                    .map(prerequisiteId -> courseService.getCourseById(prerequisiteId)
                            .map(Course::getName).orElse("Unknown Course"))
                    .collect(Collectors.toList());

            return mapper.toCourseDTO(course, instructorName, prerequisiteNames);
        }).collect(Collectors.toList());

        return new ResponseEntity<>(courseDTOs, HttpStatus.OK);
    }


    // get course by ID
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable ObjectId id) {
        Optional<Course> courseOptional = courseService.getCourseById(id);

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();

            // Retrieve instructor name
            String instructorName = userService.getUserById(course.getInstructorId())
                    .map(User::getName)
                    .orElse("Unknown Instructor");

            // Retrieve names of prerequisite courses
            List<String> prerequisiteNames = course.getPrerequisites().stream()
                    .map(prerequisiteId -> courseService.getCourseById(prerequisiteId)
                            .map(Course::getName)
                            .orElse("Unknown Course"))
                    .collect(Collectors.toList());

            // Convert to CourseDTO
            CourseDTO courseDTO = mapper.toCourseDTO(course, instructorName, prerequisiteNames);

            return new ResponseEntity<>(courseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable ObjectId id,
                                                  @RequestParam(required = false) List<ObjectId> prerequisiteIds,
                                                  @RequestBody CourseCreationDTO courseDetails) {
        // Retrieve the existing course to check its current prerequisites
        Optional<Course> existingCourseOptional = courseService.getCourseById(id);
        if (existingCourseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Course existingCourse = existingCourseOptional.get();

        // If prerequisiteIds is null, retain existing prerequisites; otherwise, update them
        List<ObjectId> updatedPrerequisites = prerequisiteIds != null ? prerequisiteIds : existingCourse.getPrerequisites();

        // Convert CourseCreationDTO to Course
        Course updatedCourse = mapper.toCourse(courseDetails);
        updatedCourse.setId(id);
        updatedCourse.setPrerequisites(updatedPrerequisites);

        if (updatedCourse.getName() == null) {
            updatedCourse.setName(existingCourse.getName());
        }

        if (updatedCourse.getDescription() == null) {
            updatedCourse.setDescription(existingCourse.getDescription());
        }

        if (updatedCourse.getCredits() == 0) {
            updatedCourse.setCredits(existingCourse.getCredits());
        }

        if (updatedCourse.getCapacity() == 0) {
            updatedCourse.setCapacity(existingCourse.getCapacity());
        }

        if (updatedCourse.getInstructorId() == null) {
            updatedCourse.setInstructorId(existingCourse.getInstructorId());
        }


        Course savedCourse = courseService.updateCourse(id, updatedCourse);

        String instructorName = userService.getUserById(savedCourse.getInstructorId())
                .map(User::getName)
                .orElse("Unknown Instructor");

        List<String> prerequisiteNames = savedCourse.getPrerequisites().stream()
                .map(prerequisiteId -> courseService.getCourseById(prerequisiteId)
                        .map(Course::getName)
                        .orElse("Unknown Course"))
                .collect(Collectors.toList());

        CourseDTO courseDTO = mapper.toCourseDTO(savedCourse, instructorName, prerequisiteNames);

        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }



    // delete course by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable ObjectId id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

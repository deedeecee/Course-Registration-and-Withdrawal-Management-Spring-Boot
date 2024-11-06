package com.debankar.course_management_system.dto;

import com.debankar.course_management_system.entity.Course;
import com.debankar.course_management_system.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapper {
    public UserDTO toUserDTO(User user) {
        String name = user.getName();
        String email = user.getEmail();
        User.Role role = user.getRole();

        return new UserDTO(name, email, role);
    }

    public User toUser(UserCreationDTO userCreationDTO) {
        return new User(
                        userCreationDTO.getName(),
                        userCreationDTO.getEmail(),
                        userCreationDTO.getPassword(),
                        userCreationDTO.getRole()
                );
    }

    public CourseDTO toCourseDTO(Course course, String instructorName, List<String> prerequisiteNames) {
        return new CourseDTO(
                course.getName(),
                course.getDescription(),
                course.getCredits(),
                prerequisiteNames,
                course.getCapacity(),
                instructorName
        );
    }

    public Course toCourse(CourseCreationDTO courseCreationDTO) {
        Course course = new Course();

        course.setName(courseCreationDTO.getName());
        course.setDescription(courseCreationDTO.getDescription());
        course.setCredits(courseCreationDTO.getCredits());
        course.setPrerequisites(courseCreationDTO.getPrerequisites());
        course.setCapacity(courseCreationDTO.getCapacity());
        course.setInstructorId(courseCreationDTO.getInstructorId());

        return course;
    }
}

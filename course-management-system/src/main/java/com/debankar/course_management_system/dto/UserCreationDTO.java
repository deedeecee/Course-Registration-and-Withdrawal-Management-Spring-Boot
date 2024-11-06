package com.debankar.course_management_system.dto;

import com.debankar.course_management_system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class UserCreationDTO {
    private String name;

    private String email;

    private String password;

    private User.Role role;
}

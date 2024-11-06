package com.debankar.course_management_system.dto;

import com.debankar.course_management_system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String name;

    private String email;

    private User.Role role;
}

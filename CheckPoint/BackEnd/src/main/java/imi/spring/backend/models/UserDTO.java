package imi.spring.backend.models;

import lombok.Data;

@Data
public class UserDTO {
    private final String username;
    private final String email;
    private final String password;
}
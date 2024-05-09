package com.BlogifyHub.model.DTO;

import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.model.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private Role role;
    private List<Post> posts;
}

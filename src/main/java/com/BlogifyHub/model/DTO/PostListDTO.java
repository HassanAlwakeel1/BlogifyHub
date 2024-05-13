package com.BlogifyHub.model.DTO;

import com.BlogifyHub.model.entity.enums.ListStatus;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostListDTO {
    private Long id;
    private ProfileResponseDTO profileResponseDTO;
    private String name;
    private String description;
    private Date createdAt;
    private ListStatus listStatus;

}

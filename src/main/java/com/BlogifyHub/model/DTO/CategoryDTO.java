package com.BlogifyHub.model.DTO;

import com.BlogifyHub.model.entity.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private CategoryType categoryType;
    private String description;
}

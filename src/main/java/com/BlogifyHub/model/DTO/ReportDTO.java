package com.BlogifyHub.model.DTO;

import com.BlogifyHub.model.entity.enums.ContentType;
import lombok.Data;

@Data
public class ReportDTO {
    private String reason;
    private Long contentId;
    private ContentType contentType;
}

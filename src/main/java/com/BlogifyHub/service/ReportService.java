package com.BlogifyHub.service;

import com.BlogifyHub.model.entity.Report;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.entity.enums.ContentType;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ReportService {
    Report createReport(Authentication authentication,
                        String reason,
                        Long contentId,
                        ContentType contentType);

    List<Report> getAllReports();

    void reviewReport(Long reportId, Authentication authentication, boolean shouldDelete, String moderatorComment);
}

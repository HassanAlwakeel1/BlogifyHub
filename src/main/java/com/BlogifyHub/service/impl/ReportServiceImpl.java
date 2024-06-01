package com.BlogifyHub.service.impl;

import com.BlogifyHub.model.entity.Report;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.entity.enums.ContentType;
import com.BlogifyHub.repository.ReportRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.ReportService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private UserRepository userRepository;
    private ReportRepository reportRepository;

    public ReportServiceImpl(UserRepository userRepository, ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
    }

    @Override
    public Report createReport(Authentication authentication,
                               String reason,
                               Long contentId,
                               ContentType contentType) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Invalid token"));

        reportRepository.findByReportedByAndContentIdAndContentType(user, contentId, contentType)
                .ifPresent(report -> {
                    throw new IllegalArgumentException("You have already reported this content.");
                });
        Report report = new Report();
        report.setReportedBy(user);
        report.setReason(reason);
        report.setContentId(contentId);
        report.setContentType(contentType);
        report.setReportedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
}

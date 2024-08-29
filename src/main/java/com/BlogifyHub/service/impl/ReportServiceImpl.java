package com.BlogifyHub.service.impl;

import com.BlogifyHub.model.entity.Report;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.entity.enums.ContentType;
import com.BlogifyHub.model.entity.enums.ReportStatus;
import com.BlogifyHub.repository.ReportRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.PostService;
import com.BlogifyHub.service.ReportService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private UserRepository userRepository;
    private ReportRepository reportRepository;

    private PostService postService;

    public ReportServiceImpl(UserRepository userRepository,
                             ReportRepository reportRepository,
                             PostService postService) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.postService = postService;
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

    @Override
    public void reviewReport(Long reportId,
                             Authentication authentication,
                             boolean shouldDelete,
                             String moderatorComment) {
        User moderator = (User) authentication.getPrincipal(); // Cast the principal to your User class
        Report report = reportRepository.findById(reportId)
                .orElseThrow(()-> new IllegalArgumentException("Report not found"));

        if (shouldDelete){
            postService.deletePostById(moderator.getId(),report.getContentId());
        }

        report.setModerator(moderator);
        report.setStatus(ReportStatus.REVIEWED);
        report.setReviewedAt(LocalDateTime.now());
        report.setModeratorComment(moderatorComment);

        reportRepository.save(report);

    }
}

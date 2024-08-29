package com.BlogifyHub.controller;

import com.BlogifyHub.model.DTO.ReportDTO;
import com.BlogifyHub.model.entity.Report;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.entity.enums.ContentType;
import com.BlogifyHub.service.ReportService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public Report createReport(Authentication authentication,
                               @RequestBody ReportDTO reportDTO) {
        String reason = reportDTO.getReason();
        Long contentId = reportDTO.getContentId();
        ContentType contentType = reportDTO.getContentType();
        return reportService.createReport(authentication,reason, contentId, contentType);
    }

    @GetMapping
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @PostMapping("/{reportId}/review")
    public void reviewReport(
            @PathVariable Long reportId,
            @RequestParam boolean shouldDelete,
            @RequestParam(required = false) String comment,
            Authentication authentication) {
        reportService.reviewReport(reportId, authentication, shouldDelete, comment);
    }
}

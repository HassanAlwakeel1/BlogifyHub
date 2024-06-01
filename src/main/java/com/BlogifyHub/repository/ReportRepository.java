package com.BlogifyHub.repository;

import com.BlogifyHub.model.entity.Report;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.entity.enums.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report,Long> {
    Optional<Report> findByReportedByAndContentIdAndContentType(User reportedBy, Long contentId, ContentType contentType);
}

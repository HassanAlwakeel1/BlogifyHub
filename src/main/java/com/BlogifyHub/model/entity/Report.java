package com.BlogifyHub.model.entity;

import com.BlogifyHub.model.entity.enums.ContentType;
import com.BlogifyHub.model.entity.enums.ReportStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User reportedBy;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(nullable = false)
    private LocalDateTime reportedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private User moderator;  // Moderator who reviewed the report

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "moderator_comment", length = 1000)
    private String moderatorComment;
}


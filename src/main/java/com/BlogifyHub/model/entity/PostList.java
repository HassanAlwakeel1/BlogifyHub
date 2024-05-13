package com.BlogifyHub.model.entity;

import com.BlogifyHub.model.entity.enums.ListStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post_list")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Column(name = "list_status")
    private ListStatus listStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_list_id",nullable = false)
    @JsonBackReference
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

}

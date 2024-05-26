package com.BlogifyHub.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "comment",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<CommentClap> commentClaps;

    @Column(name = "number_of_claps")
    private Integer numberOfClaps = 0;

    @Column(name = "number_of_clappers")
    private Integer numberOfClappers = 0;

}

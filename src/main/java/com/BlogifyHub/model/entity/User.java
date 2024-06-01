package com.BlogifyHub.model.entity;

import com.BlogifyHub.model.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name can't exceed 100 characters")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name can't exceed 100 characters")
    private String lastName;

    @Column(name = "bio")
    @Size(max = 500, message = "Bio can't exceed 300 characters.")
    private String bio;

    @Column(name = "password",nullable = false)
    @NotBlank(message = "password is required")
    @Size(max = 100, message = "Password can't exceed 100 characters")
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    //TODO : here handle the exception when someone trying to enter an email already exists in the db
    private String email;

    @Column(name = "profile_picture_url")
    private String ProfilePictureURL;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "enabled")
    private boolean enabled = false; // By default, the user is not enabled

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Token> tokens;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Post> userPosts;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Comment> userComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<PostList> userPostLists;

    @OneToMany(mappedBy = "followedUser", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Follow> followers;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Subscription> subscriptions;

    @Column(name = "subscribers_number")
    private Integer subscribersNumber = 0;

    @Column(name = "subscriptions_number")
    private Integer subscriptionsNumber = 0;

    @Column(name = "followers_number")
    private Integer followersNumber = 0;

    @Column(name = "following_number")
    private Integer followingNumber = 0;

    @OneToMany(mappedBy = "mutedUser", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Mute> mutedUser;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<PostClap> postClaps;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<CommentClap> commentClaps;

    @OneToMany(mappedBy = "reportedBy")
    @JsonManagedReference
    private List<Report> reports;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}

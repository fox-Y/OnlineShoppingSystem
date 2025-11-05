package org.example.onlineshoppingsystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.onlineshoppingsystem.domain.enums.Role;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(nullable = false)
    private Instant createdDate;

    @PrePersist
    void pre() {
        if (createdDate == null) {
            createdDate = Instant.now();
        }

        if (role == null) {
            role = Role.USER;
        }
    }
}

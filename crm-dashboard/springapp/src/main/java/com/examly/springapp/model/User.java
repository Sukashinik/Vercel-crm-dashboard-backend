package com.examly.springapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    public enum Role {
        ADMIN, SALES_REP, ANALYST
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String email;

    private String name;   
    
    private String phone;
    
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;  
}

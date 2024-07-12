package com.sparta.trello.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    @OneToOne
    private Role role;

    @ManyToOne
    private User user;


//    public UserRole(String role) {
//        this.role = role;
//    }

}
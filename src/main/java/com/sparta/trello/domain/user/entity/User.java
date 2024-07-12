package com.sparta.trello.domain.user.entity;

import com.sparta.trello.domain.auth.entity.Auth;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardInvitation;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String refreshToken;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToOne
    @JoinColumn(name = "auth_id")
    private Auth auth;

//    @ManyToMany
//    @JoinTable(
//            name = "user_role_matches",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role")
//    )
//    private List<UserRole> userRoles;

    @OneToMany
    @JoinColumn(name =  "boardInvitationId")
    private List<BoardInvitation> boardInvitationList= new ArrayList<>();

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public boolean equals(Object o) {
        UserDetails userDetails = (UserDetails) o;
        return email.equals(userDetails.getUsername());
    }
}

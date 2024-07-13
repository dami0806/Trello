package com.sparta.trello.domain.boardInvitaion.dto;


import com.sparta.trello.domain.userRole.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardInvitationRequest {
    private String email;
    private String role;
}

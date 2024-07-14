package com.sparta.trello.domain.user;

import com.sparta.trello.domain.auth.service.UserService;
import com.sparta.trello.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/not-invited/{boardId}")
    public ResponseEntity<List<UserResponse>> getUsersNotInvitedToBoard(@PathVariable Long boardId) {
        List<UserResponse> users = userService.getUsersNotInvitedToBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/invited/{boardId}")
    public ResponseEntity<List<UserResponse>> getUsersInvitedToBoard(@PathVariable Long boardId) {
        List<UserResponse> users = userService.getUsersInvitedToBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}

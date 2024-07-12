package com.sparta.trello.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

public enum UserRole {
    USER,
    MANAGER
}
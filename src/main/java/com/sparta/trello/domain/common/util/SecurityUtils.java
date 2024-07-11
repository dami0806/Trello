package com.sparta.trello.domain.common.util;

import com.sparta.trello.domain.user.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    // 현재 인증된 사용자 정보를 반환
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

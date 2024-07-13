package com.sparta.trello.domain.user.repository;

import com.sparta.trello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> ,UserRepositoryCustom{
    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String email);

}

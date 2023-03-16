package ru.netology.cloudapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloudapi.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}

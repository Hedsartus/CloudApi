package ru.netology.cloudapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloudapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String username);
}

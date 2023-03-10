package ru.netology.cloudapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloudapi.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

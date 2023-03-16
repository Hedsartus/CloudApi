package ru.netology.cloudapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.netology.cloudapi.model.FileEntity;
import ru.netology.cloudapi.model.User;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    @Query(value = "SELECT * FROM files f WHERE f.user_id = :userid LIMIT :limitCount", nativeQuery = true)
    List<FileEntity> findByUserIdAndLimit(
            @Param("userid") Long userId,
            @Param("limitCount") int limitCount);

    Boolean existsByNameAndUser(String filename, User user);

    void removeByNameAndUser_Id(String filename, Long idUser);

    Optional<FileEntity> findByNameAndUser(String filename, User user);
}

package com.example.capstone_project.repository;

import com.example.capstone_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findUserByUsername(String username);

    @Query(value = "select user from User user " +
            "join fetch user.role " +
            "join fetch user.department " +
            "join fetch user.position " +
            "join fetch user.userSetting " +
            "where user.username = :username and (user.isDelete != false or user.isDelete is null)")
    Optional<User> findUserDetailedByUsername(String username);

    Optional<User> findUserById(Long userId);

    @Query(value = "select user from User user " +
            "join fetch user.role " +
            "join fetch user.department " +
            "join fetch user.position " +
            "join fetch user.userSetting " +
            "where user.id = :userId and (user.isDelete != false or user.isDelete is null)")
    Optional<User> findUserDetailedById(Long userId);

    @Query(value = "select user from User user " +
            "join fetch user.role " +
            "join fetch user.department " +
            "where user.id = :userId and (user.isDelete != false or user.isDelete is null)"
    )
    Optional<User> findUserWithRoleAndDepartmentById(Long userId);

    @Query(value = "select distinct count(user.id) from User user " +
            "where user.username like %:query% and (user.isDelete != false or user.isDelete is null)")
    long countDistinct(String query);
}

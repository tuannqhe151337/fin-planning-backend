package com.example.capstone_project.repository;

import com.example.capstone_project.entity.RoleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, Long> {
    boolean existsById(Long id);
}

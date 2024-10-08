package com.example.capstone_project.repository;

import com.example.capstone_project.entity.TermStatus;
import com.example.capstone_project.utils.enums.TermStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermStatusRepository extends JpaRepository<TermStatus, Long> {
    @Override
    List<TermStatus> findAll();

    @Override
    <S extends TermStatus> List<S> saveAll(Iterable<S> entities);

    TermStatus findByCode(TermStatusCode termCode);
}

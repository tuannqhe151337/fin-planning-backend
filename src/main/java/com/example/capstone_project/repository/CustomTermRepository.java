package com.example.capstone_project.repository;


import com.example.capstone_project.entity.Term;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomTermRepository {

    List<Term> getListTermPaging(String query, Pageable pageable);

    List<Term> getListTermWhenCreatePlan(String query, Pageable pageable, Long departmentId);
}

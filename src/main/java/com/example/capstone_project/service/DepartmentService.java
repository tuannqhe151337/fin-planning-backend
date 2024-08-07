package com.example.capstone_project.service;

import com.example.capstone_project.entity.Department;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface DepartmentService {

    long countDistinct(String query);

    List<Department> getListDepartmentPaging(String query, Pageable pageable);

    long countDistinctListDepartmentPaging(String query);

    void createDepartment(String departmentName) throws Exception;

    void deleteDepartment(Long departmentId) throws Exception;

    void updateDepartment(Department department) throws Exception;

    List<Department> getListDepartment();
}
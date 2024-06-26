package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "annual_reports")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnualReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "total_term")
    private Integer totalTerm;

    @Column(name = "total_department")
    private Integer totalDepartment;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private Boolean isDelete;

    @OneToMany(mappedBy = Report_.ANNUAL_REPORT)
    private List<Report> reports;
}

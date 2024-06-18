package com.example.capstone_project.entity;

import com.example.capstone_project.utils.enums.PlanStatusCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "financial_plans")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class FinancialPlan extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Transient
    private Integer version;

    @OneToMany(mappedBy = FinancialPlanFile_.PLAN)
    private List<FinancialPlanFile> planFiles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private PlanStatus status;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;
}

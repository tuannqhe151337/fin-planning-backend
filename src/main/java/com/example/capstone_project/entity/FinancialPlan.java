package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(schema = "capstone_v2", name = "financial_plans")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class FinancialPlan extends BaseEntity {
    // https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    // https://stackoverflow.com/questions/17780394/hibernate-identity-vs-sequence-entity-identifier-generators
    // GenerationType.IDENTITY will disable batch insert!!!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @Transient
    private Integer version;

    @Column(name = "actual_cost")
    private BigDecimal actualCost;

    @Column(name = "expected_cost")
    private BigDecimal expectedCost;

    @OneToMany(mappedBy = FinancialPlanFile_.PLAN, cascade = CascadeType.ALL)
    private List<FinancialPlanFile> planFiles;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "term_id")
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;
}

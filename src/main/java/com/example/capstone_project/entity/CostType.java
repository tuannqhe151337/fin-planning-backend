package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "cost_types")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class CostType extends BaseEntity {
    // https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    // https://stackoverflow.com/questions/17780394/hibernate-identity-vs-sequence-entity-identifier-generators
    // GenerationType.IDENTITY will disable batch insert!!!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @OneToMany(mappedBy = MonthlyReportSummary_.COST_TYPE)
    private List<MonthlyReportSummary> monthlyReportSummaries;

    @OneToMany(mappedBy = FinancialPlanExpense_.COST_TYPE)
    private List<FinancialPlanExpense> planExpenses;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private boolean isDelete;

}

package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(schema = "capstone_v2", name = "financial_plan_expenses")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class FinancialPlanExpense extends BaseEntity {
    // https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    // https://stackoverflow.com/questions/17780394/hibernate-identity-vs-sequence-entity-identifier-generators
    // GenerationType.IDENTITY will disable batch insert!!!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "financial_plan_expense_key", columnDefinition = "NVARCHAR(255)")
    private String planExpenseKey;

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "pic_id")
    private User pic;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "status_id")
    private ExpenseStatus status;

    @OneToMany(mappedBy = FinancialPlanFileExpense_.PLAN_EXPENSE)
    private List<FinancialPlanFileExpense> files;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_type_id")
    private CostType costType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;

}

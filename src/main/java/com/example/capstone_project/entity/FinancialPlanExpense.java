package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "financial_plan_expenses")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FinancialPlanExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "financial_plan_expense_key")
    private String financialPlanExpenseKey;

    @Column(name = "name")
    private String name;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "pic")
    private String pic;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private FinancialStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_plan_file_id")
    private FinancialPlanFile financialPlanFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_type_id")
    private CostType costType;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDate updatedAt;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private Boolean isDelete;
}

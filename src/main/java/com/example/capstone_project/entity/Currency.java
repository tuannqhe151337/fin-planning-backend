package com.example.capstone_project.entity;

import com.example.capstone_project.utils.enums.Affix;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(schema = "capstone_v2", name = "currency")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Currency extends BaseEntity {
    // https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    // https://stackoverflow.com/questions/17780394/hibernate-identity-vs-sequence-entity-identifier-generators
    // GenerationType.IDENTITY will disable batch insert!!!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "symbol", columnDefinition = "NVARCHAR(10)")
    private String symbol;

    @Column(name = "affrix")
    @Enumerated(EnumType.STRING)
    private Affix affix;

    @OneToMany(mappedBy = CurrencyExchangeRate_.CURRENCY)
    private List<CurrencyExchangeRate> currencyExchangeRates;

    @Column(name = "isDefault", updatable = false)
    private boolean isDefault;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;
}

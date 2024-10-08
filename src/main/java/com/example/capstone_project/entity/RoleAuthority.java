package com.example.capstone_project.entity;

import com.zaxxer.hikari.util.ClockSource;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
@Entity
@Table(schema = "capstone_v2",name = "role_authority")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class RoleAuthority extends BaseEntity{
    // https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    // https://stackoverflow.com/questions/17780394/hibernate-identity-vs-sequence-entity-identifier-generators
    // GenerationType.IDENTITY will disable batch insert!!!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;
}

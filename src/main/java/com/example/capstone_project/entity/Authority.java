package com.example.capstone_project.entity;

import com.example.capstone_project.utils.enums.AuthorityCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "authorities")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Authority extends BaseEntity {
    // https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    // https://stackoverflow.com/questions/17780394/hibernate-identity-vs-sequence-entity-identifier-generators
    // GenerationType.IDENTITY will disable batch insert!!!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "code", columnDefinition = "NVARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private AuthorityCode code;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private Boolean isDelete;

    @ToString.Exclude
    @OneToMany(mappedBy = "authority")
    private List<RoleAuthority> roleAuthorities;
}

package com.example.capstone_project.entity;

import com.example.capstone_project.utils.enums.TermDuration;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "terms")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
public class Term extends BaseEntity{
    // https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    // https://stackoverflow.com/questions/17780394/hibernate-identity-vs-sequence-entity-identifier-generators
    // GenerationType.IDENTITY will disable batch insert!!!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @NotNull(message = "Duration cannot be empty")
    @Column(name = "duration")
    @Enumerated(EnumType.STRING)
    private TermDuration duration ;

    @NotNull(message = "Start date cannot be null")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
   // @Future(message = "End date must be in the future")
    @Column(name = "end_date")
    private LocalDateTime endDate;


  // @Future(message = "Re-upload start date must be in the future")
    @Column(name = "start_reupload_date")
    private LocalDateTime reuploadStartDate;



   // @Future(message = "Re-upload end date must be in the future")
    @Column(name = "end_reupload_date")
    private LocalDateTime reuploadEndDate;

    @NotNull(message = "Final end date cannot be null")
    //@Future(message = "Final end date must be in the future")
    @Column(name = "final_end_term")
    private LocalDateTime finalEndTermDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "status_id")
    private TermStatus status; //trong day da co isDelete roi

    @OneToMany(mappedBy = "term", fetch = FetchType.LAZY)
    private List<FinancialPlan> financialPlans;

    @OneToMany(mappedBy = "term", fetch = FetchType.LAZY)
    private List<FinancialReport> financialReports;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;

    @NotNull(message = "allowReupload cannot be null")
    @Column(name = "allow_reupload", columnDefinition = "bit default 0")
    private boolean allowReupload;

}

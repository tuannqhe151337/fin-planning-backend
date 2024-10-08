package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Term;
import com.example.capstone_project.utils.enums.TermStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long>, CustomTermRepository {

    @Query(" SELECT CASE " +
            " WHEN EXISTS (SELECT 1 FROM Term term " +
            " JOIN term.financialPlans plan " +
            " WHERE term.id = :termId AND " +
            " plan.department.id = :departmentId AND " +
            " plan.isDelete = false )" +
            " THEN true " +
            " ELSE false " +
            " END ")
    boolean existsPlanOfDepartmentInTerm(long departmentId, Long termId);

    @Query(value = " SELECT count(distinct (term.id) ) FROM Term term " +
            " LEFT JOIN term.financialPlans plan" +
            " WHERE term.name like %:query% AND " +
            " term.id NOT IN (SELECT t.id FROM Term t JOIN t.financialPlans p WHERE p.department.id = :departmentId) AND " +
            " term.status.code != :close AND " +
            " (term.startDate <= :now AND term.endDate >= :now) AND " +
            " term.isDelete = false ")
    long countDistinctListTermWhenCreatePlan(@Param("query") String query,
                                             @Param("close") TermStatusCode close, @Param("now") LocalDateTime now,
                                             @Param("departmentId") Long departmentId);

    @Query(value = "SELECT count(distinct (term.id)) FROM Term term " +
            " WHERE term.name like %:query% AND " +
            " term.status.name != :close AND " +
            " term.endDate >= :now AND " +
            " term.isDelete = false ")
    long countDistinctListTermWhenCreatePlan(@Param("query") String query, @Param("close") String close,
                                             @Param("now") LocalDateTime now);

    @Query(value = " SELECT count(distinct (term.id)) FROM Term term " +
            " WHERE term.name like %:query% AND " +
            "(:statusId IS NULL OR term.status.id = :statusId) AND " +
            " term.isDelete = false ")
    long countDistinctListTermPaging(@Param("statusId") Long statusId, @Param("query") String query);

    //crud term
    Term findTermById(Long id);

    @Override
    boolean existsById(Long aLong);

    @Query(value = " SELECT term FROM Term term " +
            " WHERE term.status.code = :termCode AND " +
            " cast(term.endDate as localdate) = cast(:now as localdate) AND " +
            " term.isDelete = false ")
    List<Term> getListTermNeedToStart(TermStatusCode termCode, LocalDateTime now);

    @Query(value = " SELECT term FROM Term term " +
            " WHERE term.status.code = :termCode AND " +
            " cast(term.startDate as localdate) = cast(:now as localdate) AND " +
            " term.isDelete = false ")
    List<Term> getListTermNeedToClose(TermStatusCode termCode, LocalDateTime now);

    @Query( " SELECT term FROM FinancialPlan plan " +
            " JOIN plan.term term " +
            " WHERE plan.id = :planId AND " +
            " term.isDelete = false OR term.isDelete is null ")
    Term getTermByPlanId(Long planId);

    @Query(value = " SELECT term FROM Term term " +
            " WHERE cast(term.startDate as localdate) <= cast(:now as localdate) AND " +
            " term.isDelete = false ")
    List<Term> getListTermNeedToStartForSeed(LocalDateTime now);

    @Query(value = " SELECT term FROM Term term " +
            " WHERE cast(term.startDate as localdate) <= cast(:now as localdate) AND " +
            " term.isDelete = false ")
    List<Term> getListTermNeedToCloseForSeed(LocalDateTime now);
}

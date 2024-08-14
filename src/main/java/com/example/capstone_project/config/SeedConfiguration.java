package com.example.capstone_project.config;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.utils.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class SeedConfiguration {
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner commandLineRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            AuthorityRepository authorityRepository,
            RoleAuthorityRepository roleAuthorityRepository,
            DepartmentRepository departmentRepository,
            PositionRepository positionRepository,
            UserSettingRepository userSettingRepository,
            FinancialPlanRepository planRepository,
            TermRepository termRepository,
            TermStatusRepository termStatusRepository,
            ReportStatusRepository reportStatusRepository,
            CostTypeRepository costTypeRepository,
            ExpenseStatusRepository expenseStatusRepository,
            FinancialPlanFileRepository financialPlanFileRepository,
            FinancialPlanFileExpenseRepository financialPlanFileExpenseRepository,
            FinancialPlanExpenseRepository financialPlanExpenseRepository,
            FinancialReportRepository financialReportRepository,
            AnnualReportRepository annualReportRepository,
            ReportRepository reportRepository,
            SupplierRepository supplierRepository,
            ProjectRepository projectRepository,
            TermIntervalRepository termIntervalRepository,
            CurrencyRepository currencyRepository,
            CurrencyExchangeRateRepository currencyExchangeRateRepository,
            ReportStatisticalRepository reportStatisticalRepository

    ) {
        return args -> {
            if (System.getenv("SPRING_PROFILES_ACTIVE") != null && System.getenv("SPRING_PROFILES_ACTIVE").equals("prod")) {
                return;
            }

            //Term Status - fixed code
            TermStatus termStatus = TermStatus.
                    builder()
                    .id(1L).
                    name("New")
                    .code(TermCode.NEW).build();

            //Term Status - fixed code
            TermStatus termStatus2 = TermStatus.
                    builder()
                    .id(2L).
                    name("In progress")
                    .code(TermCode.IN_PROGRESS).build();

            //Term Status - fixed code
            TermStatus termStatus3 = TermStatus.
                    builder()
                    .id(3L).
                    name("Closed")
                    .code(TermCode.CLOSED).build();

            termStatusRepository.saveAll(List.of(termStatus, termStatus2, termStatus3));

            // Term interval date
            TermInterval termInterval = TermInterval
                    .builder()
                    .id(1L)
                    .startTermDate(25)
                    .endTermInterval(5)
                    .startReuploadInterval(20)
                    .endReuploadInterval(3)
                    .build();
            termIntervalRepository.save(termInterval);

            // Department
            Department itDepartment = Department.builder()
                    .id(1L)
                    .name("IT Department")
                    .build();

            Department hrDepartment = Department.builder()
                    .id(2L)
                    .name("HR Department")
                    .build();

            Department financeDepartment = Department.builder()
                    .id(3L)
                    .name("Finance Department")
                    .build();

            Department communicationDepartment = Department.builder()
                    .id(4L)
                    .name("Communication Department")
                    .build();

            Department marketingDepartment = Department.builder()
                    .id(5L)
                    .name("Marketing Department")
                    .build();

            Department accountingDepartment = Department.builder()
                    .id(6L)
                    .name("Accounting Department")
                    .build();

            departmentRepository.saveAll(List.of(itDepartment, hrDepartment, financeDepartment, communicationDepartment, marketingDepartment, accountingDepartment));

            // Position
            Position techlead = Position.builder()
                    .name("Techlead")
                    .build();

            Position staff = Position.builder()
                    .name("Staff")
                    .build();

            Position juniorDev = Position.builder()
                    .name("Junior development")
                    .build();

            positionRepository.saveAll(List.of(techlead, staff, juniorDev));

            // Authority
            Authority createUser = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_USER)
                    .name("Create new user")
                    .build();

            Authority viewListUsers = Authority.builder()
                    .code(AuthorityCode.VIEW_LIST_USERS)
                    .name("View list users")
                    .build();

            Authority deleteUser = Authority.builder()
                    .code(AuthorityCode.DELETE_USER)
                    .name("Delete user")
                    .build();

            Authority editUser = Authority.builder()
                    .code(AuthorityCode.EDIT_USER)
                    .name("Edit user")
                    .build();

            Authority activateUser = Authority.builder()
                    .code(AuthorityCode.ACTIVATE_USER)
                    .name("Activate user")
                    .build();

            Authority deactivateUser = Authority.builder()
                    .code(AuthorityCode.DEACTIVATE_USER)
                    .name("Deactivate user")
                    .build();

            Authority viewUserDetail = Authority.builder()
                    .code(AuthorityCode.VIEW_USER_DETAILS)
                    .name("View user detail")
                    .build();

            Authority createTerm = Authority.builder()
                    .code(AuthorityCode.CREATE_TERM)
                    .name("Create term")
                    .build();

            Authority editTerm = Authority.builder()
                    .code(AuthorityCode.EDIT_TERM)
                    .name("Edit term")
                    .build();

            Authority viewTerm = Authority.builder()
                    .code(AuthorityCode.VIEW_TERM)
                    .name("View term")
                    .build();

            Authority startTerm = Authority.builder()
                    .code(AuthorityCode.START_TERM)
                    .name("Start term")
                    .build();

            Authority deleteTerm = Authority.builder()
                    .code(AuthorityCode.DELETE_TERM)
                    .name("Delete term")
                    .build();

            Authority importPlan = Authority.builder()
                    .code(AuthorityCode.IMPORT_PLAN)
                    .name("Import plan")
                    .build();

            Authority reUploadPlan = Authority.builder()
                    .code(AuthorityCode.RE_UPLOAD_PLAN)
                    .name("Reupload plan")
                    .build();

            Authority submitPlanForReview = Authority.builder()
                    .code(AuthorityCode.SUBMIT_PLAN_FOR_REVIEW)
                    .name("Submit plan for review")
                    .build();

            Authority deletePlan = Authority.builder()
                    .code(AuthorityCode.DELETE_PLAN)
                    .name("Delete plan")
                    .build();

            Authority downloadPlan = Authority.builder()
                    .code(AuthorityCode.DOWNLOAD_PLAN)
                    .name("Download plan")
                    .build();

            Authority approvePlan = Authority.builder()
                    .code(AuthorityCode.APPROVE_PLAN)
                    .name("Approve plan")
                    .build();

            Authority viewPlan = Authority.builder()
                    .code(AuthorityCode.VIEW_PLAN)
                    .name("View plan")
                    .build();

            Authority viewReport = Authority.builder() // Monthly, Quarterly, Half-year
                    .code(AuthorityCode.VIEW_REPORT)
                    .name("View report")
                    .build();

            Authority downloadReport = Authority.builder() // Monthly, Quarterly, Half-year
                    .code(AuthorityCode.DOWNLOAD_REPORT)
                    .name("Download report")
                    .build();

            Authority deleteReport = Authority.builder()
                    .code(AuthorityCode.DELETE_REPORT)
                    .name("Delete report")
                    .build();

            Authority viewAnnualReport = Authority.builder()
                    .code(AuthorityCode.VIEW_ANNUAL_REPORT)
                    .name("View annual report")
                    .build();

            Authority downloadAnnualReport = Authority.builder()
                    .code(AuthorityCode.DOWNLOAD_ANNUAL_REPORT)
                    .name("Download annual report")
                    .build();

            Authority createNewDepartment = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_DEPARTMENT)
                    .name("Create new department")
                    .build();

            Authority updateDepartment = Authority.builder()
                    .code(AuthorityCode.UPDATE_DEPARTMENT)
                    .name("Update department")
                    .build();

            Authority deleteDepartment = Authority.builder()
                    .code(AuthorityCode.DELETE_DEPARTMENT)
                    .name("Delete department")
                    .build();

            Authority createNewCostType = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_COST_TYPE)
                    .name("Create new cost type")
                    .build();

            Authority updateCostType = Authority.builder()
                    .code(AuthorityCode.UPDATE_COST_TYPE)
                    .name("Update cost type")
                    .build();

            Authority deleteCostType = Authority.builder()
                    .code(AuthorityCode.DELETE_COST_TYPE)
                    .name("Delete cost type")
                    .build();

            Authority createNewPosition = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_POSITION)
                    .name("Create new position")
                    .build();

            Authority updatePosition = Authority.builder()
                    .code(AuthorityCode.UPDATE_POSITION)
                    .name("Update position")
                    .build();

            Authority deletePosition = Authority.builder()
                    .code(AuthorityCode.DELETE_POSITION)
                    .name("Delete position")
                    .build();

            Authority viewPosition = Authority.builder()
                    .code(AuthorityCode.VIEW_POSITION)
                    .name("View position")
                    .build();

            Authority createNewProject = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_PROJECT)
                    .name("Create new project")
                    .build();

            Authority updateProject = Authority.builder()
                    .code(AuthorityCode.UPDATE_PROJECT)
                    .name("Update project")
                    .build();

            Authority deleteProject = Authority.builder()
                    .code(AuthorityCode.DELETE_PROJECT)
                    .name("Delete project")
                    .build();

            Authority viewProject = Authority.builder()
                    .code(AuthorityCode.VIEW_PROJECT)
                    .name("View project")
                    .build();

            Authority createNewSupplier = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_SUPPLIER)
                    .name("Create new supplier")
                    .build();

            Authority updateSupplier = Authority.builder()
                    .code(AuthorityCode.UPDATE_SUPPLIER)
                    .name("Update supplier")
                    .build();

            Authority deleteSupplier = Authority.builder()
                    .code(AuthorityCode.DELETE_SUPPLIER)
                    .name("Delete supplier")
                    .build();

            Authority viewSupplier = Authority.builder()
                    .code(AuthorityCode.VIEW_SUPPLIER)
                    .name("View supplier")
                    .build();

            Authority createNewExchange = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_EXCHANGE)
                    .name("Create new exchange")
                    .build();

            Authority updateExchange = Authority.builder()
                    .code(AuthorityCode.UPDATE_EXCHANGE)
                    .name("Update exchange")
                    .build();

            Authority deleteExchange = Authority.builder()
                    .code(AuthorityCode.DELETE_EXCHANGE)
                    .name("Delete exchange")
                    .build();

            Authority viewExchange = Authority.builder()
                    .code(AuthorityCode.VIEW_EXCHANGE)
                    .name("View exchange")
                    .build();

            Authority createNewCurrency = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_CURRENCY)
                    .name("Create new currency")
                    .build();

            Authority updateCurrency = Authority.builder()
                    .code(AuthorityCode.UPDATE_CURRENCY)
                    .name("Update exchange")
                    .build();

            Authority deleteCurrency = Authority.builder()
                    .code(AuthorityCode.DELETE_CURRENCY)
                    .name("Delete currency")
                    .build();

            Authority viewCurrency = Authority.builder()
                    .code(AuthorityCode.VIEW_CURRENCY)
                    .name("View currency")
                    .build();

            authorityRepository.saveAll(List.of(viewUserDetail, viewPlan, createUser, viewListUsers, deleteUser, editUser, activateUser, deactivateUser, createTerm, editTerm, viewTerm, startTerm, deleteTerm, importPlan, reUploadPlan, submitPlanForReview, deletePlan, downloadPlan, approvePlan, viewReport, downloadReport, deleteReport, viewAnnualReport, downloadAnnualReport, createNewDepartment, updateDepartment, deleteDepartment, createNewCostType, deleteCostType, updateCostType, viewPosition, createNewPosition, updatePosition, deletePosition, viewProject, createNewProject, updateProject, deleteProject, createNewSupplier, updateSupplier, deleteSupplier, viewSupplier, createNewExchange, updateExchange, deleteExchange, viewExchange, createNewCurrency, updateCurrency, deleteCurrency, viewCurrency));

            // Role
            Role admin = Role.builder()
                    .code("admin")
                    .name("Admin")
                    .build();

            Role accountant = Role.builder()
                    .code("accountant")
                    .name("Accountant")
                    .build();

            Role financialStaff = Role.builder()
                    .code("financial-staff")
                    .name("Financial Staff")
                    .build();

            roleRepository.saveAll(List.of(admin, accountant, financialStaff));

            // User
            User user1 = User.builder()
                    .id(1L)
                    .username("username1")
                    .fullName("Nutalomlok Nunu")
                    .password(this.passwordEncoder.encode("password"))
                    .role(admin)
                    .department(itDepartment)
                    .position(techlead)
                    .email("username1@email.com")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .phoneNumber("0999988877")
                    .build();

            User user2 = User.builder()
                    .id(2L)
                    .username("username2")
                    .fullName("Sitchana Jaejan")
                    .password(this.passwordEncoder.encode("password"))
                    .role(admin)
                    .department(itDepartment)
                    .isDelete(false)
                    .phoneNumber("0999988877")
                    .position(techlead)
                    .dob(LocalDateTime.of(2000, 4, 5, 0, 0))
                    .email("username2@email.com")
                    .address("Da Nang")
                    .build();

            User user3 = User.builder()
                    .id(3L)
                    .username("username3")
                    .fullName("Nguyen The Ngoc")
                    .password(this.passwordEncoder.encode("password"))
                    .role(accountant)
                    .department(financeDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(1998, 4, 2, 2, 3))
                    .email("username3@email.com")
                    .address("Ha Noi ")
                    .phoneNumber("0999988877")
                    .isDelete(false)
                    .build();

            User user4 = User.builder()
                    .id(4L)
                    .username("username4")
                    .fullName("Choi Woo je")
                    .password(this.passwordEncoder.encode("password"))
                    .role(accountant)
                    .department(financeDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(1986, 12, 20, 0, 0))
                    .isDelete(false)
                    .email("username4@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .build();

            User user5 = User.builder()
                    .id(5L)
                    .username("username5")
                    .fullName("Nguyen The Ngoc")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(communicationDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("username5@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user6 = User.builder()
                    .id(6L)
                    .username("TuNM46")
                    .fullName("Nguyen Manh Tu")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(marketingDepartment)
                    .position(juniorDev)
                    .email("TuNM46@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2001, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user7 = User.builder()
                    .id(7L)
                    .username("TuanNQ47")
                    .fullName("Ngo Quoc Tuan")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .email("TuanNQ47@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2001, 9, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user8 = User.builder()
                    .id(8L)
                    .username("HoanNB3")
                    .fullName("Nguyen Ba Hoan")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("HoanNB3@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .isDelete(false)
                    .build();

            User user9 = User.builder()
                    .id(9L)
                    .username("BaoNN15")
                    .fullName("Nguyen Ngoc Bao")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(1997, 4, 2, 2, 3))
                    .email("BaoNN15@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .isDelete(false)
                    .build();

            User user10 = User.builder()
                    .id(10L)
                    .username("GiangDV9")
                    .fullName("Dang Van Giang")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("GiangDV9@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            userRepository.saveAll(List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10));

            // User setting
            UserSetting userSetting1 = UserSetting.builder()
                    .user(user1)
                    .darkMode(false)
                    .theme("blue")
                    .language("vi")
                    .build();

            UserSetting userSetting2 = UserSetting.builder()
                    .user(user2)
                    .darkMode(false)
                    .theme("green")
                    .language("en")
                    .build();

            UserSetting userSetting3 = UserSetting.builder()
                    .user(user3)
                    .darkMode(false)
                    .theme("green")
                    .language("en")
                    .build();

            UserSetting userSetting4 = UserSetting.builder()
                    .user(user4)
                    .darkMode(false)
                    .theme("teal")
                    .language("en")
                    .build();

            UserSetting userSetting5 = UserSetting.builder()
                    .user(user5)
                    .darkMode(false)
                    .theme("teal")
                    .language("en")
                    .build();

            UserSetting userSetting6 = UserSetting.builder()
                    .user(user6)
                    .darkMode(false)
                    .theme("teal")
                    .language("en")
                    .build();

            UserSetting userSetting7 = UserSetting.builder()
                    .user(user7)
                    .darkMode(false)
                    .theme("teal")
                    .language("en")
                    .build();

            UserSetting userSetting8 = UserSetting.builder()
                    .user(user8)
                    .darkMode(false)
                    .theme("teal")
                    .language("en")
                    .build();

            UserSetting userSetting9 = UserSetting.builder()
                    .user(user9)
                    .darkMode(false)
                    .theme("teal")
                    .language("en")
                    .build();

            UserSetting userSetting10 = UserSetting.builder()
                    .user(user10)
                    .darkMode(false)
                    .theme("teal")
                    .language("en")
                    .build();

            userSettingRepository.saveAll(List.of(userSetting1, userSetting2, userSetting3, userSetting4, userSetting5, userSetting6, userSetting7, userSetting8, userSetting9, userSetting10));

            // Role authorities
            RoleAuthority adminAuthority1 = RoleAuthority.builder()
                    .role(admin)
                    .authority(createUser)
                    .build();

            RoleAuthority adminAuthority2 = RoleAuthority.builder()
                    .role(admin)
                    .authority(viewListUsers)
                    .build();

            RoleAuthority adminAuthority3 = RoleAuthority.builder()
                    .role(admin)
                    .authority(deleteUser)
                    .build();

            RoleAuthority adminAuthority4 = RoleAuthority.builder()
                    .role(admin)
                    .authority(editUser)
                    .build();

            RoleAuthority adminAuthority5 = RoleAuthority.builder()
                    .role(admin)
                    .authority(activateUser)
                    .build();

            RoleAuthority adminAuthority6 = RoleAuthority.builder()
                    .role(admin)
                    .authority(deactivateUser)
                    .build();

            RoleAuthority adminAuthority7 = RoleAuthority.builder()
                    .role(admin)
                    .authority(viewUserDetail)
                    .build();

            RoleAuthority adminAuthority8 = RoleAuthority.builder()
                    .role(admin)
                    .authority(deleteDepartment)
                    .build();

            RoleAuthority adminAuthority9 = RoleAuthority.builder()
                    .role(admin)
                    .authority(createNewDepartment)
                    .build();

            RoleAuthority adminAuthority10 = RoleAuthority.builder()
                    .role(admin)
                    .authority(updateDepartment)
                    .build();

            RoleAuthority adminAuthority14 = RoleAuthority.builder()
                    .role(admin)
                    .authority(viewPosition)
                    .build();

            RoleAuthority adminAuthority15 = RoleAuthority.builder()
                    .role(admin)
                    .authority(createNewPosition)
                    .build();

            RoleAuthority adminAuthority16 = RoleAuthority.builder()
                    .role(admin)
                    .authority(updatePosition)
                    .build();

            RoleAuthority adminAuthority17 = RoleAuthority.builder()
                    .role(admin)
                    .authority(deletePosition)
                    .build();


            RoleAuthority accountantAuthority1 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createTerm)
                    .build();

            RoleAuthority accountantAuthority2 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(editTerm)
                    .build();

            RoleAuthority accountantAuthority3 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewTerm)
                    .build();

            RoleAuthority accountantAuthority4 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(startTerm)
                    .build();

            RoleAuthority accountantAuthority5 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteTerm)
                    .build();

            RoleAuthority accountantAuthority6 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(importPlan)
                    .build();

            RoleAuthority accountantAuthority7 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(reUploadPlan)
                    .build();

            RoleAuthority accountantAuthority8 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(submitPlanForReview)
                    .build();

            RoleAuthority accountantAuthority9 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deletePlan)
                    .build();

            RoleAuthority accountantAuthority10 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(downloadPlan)
                    .build();

            RoleAuthority accountantAuthority11 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(approvePlan)
                    .build();

            RoleAuthority accountantAuthority12 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewReport)
                    .build();

            RoleAuthority accountantAuthority13 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(downloadReport)
                    .build();

            RoleAuthority accountantAuthority14 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewAnnualReport)
                    .build();

            RoleAuthority accountantAuthority15 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(downloadAnnualReport)
                    .build();

            RoleAuthority accountantAuthority16 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewPlan)
                    .build();

            RoleAuthority accountantAuthority17 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteReport)
                    .build();

            RoleAuthority accountantAuthority18 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createNewCostType)
                    .build();

            RoleAuthority accountantAuthority19 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteCostType)
                    .build();

            RoleAuthority accountantAuthority20 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(updateCostType)
                    .build();

            RoleAuthority accountantAuthority21 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewProject)
                    .build();

            RoleAuthority accountantAuthority22 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createNewProject)
                    .build();

            RoleAuthority accountantAuthority23 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(updateProject)
                    .build();

            RoleAuthority accountantAuthority24 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteProject)
                    .build();

            RoleAuthority accountantAuthority25 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewSupplier)
                    .build();

            RoleAuthority accountantAuthority26 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createNewSupplier)
                    .build();

            RoleAuthority accountantAuthority27 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(updateSupplier)
                    .build();

            RoleAuthority accountantAuthority28 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteSupplier)
                    .build();

            RoleAuthority accountantAuthority29 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewExchange)
                    .build();

            RoleAuthority accountantAuthority30 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createNewExchange)
                    .build();

            RoleAuthority accountantAuthority31 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(updateExchange)
                    .build();

            RoleAuthority accountantAuthority32 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteExchange)
                    .build();

            RoleAuthority accountantAuthority33 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewCurrency)
                    .build();

            RoleAuthority accountantAuthority34 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createNewCurrency)
                    .build();

            RoleAuthority accountantAuthority35 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(updateCurrency)
                    .build();

            RoleAuthority accountantAuthority36 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteCurrency)
                    .build();

            RoleAuthority financialStaffAuthority6 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(importPlan)
                    .build();

            RoleAuthority financialStaffAuthority7 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(reUploadPlan)
                    .build();

            RoleAuthority financialStaffAuthority8 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(submitPlanForReview)
                    .build();

            RoleAuthority financialStaffAuthority9 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(deletePlan)
                    .build();

            RoleAuthority financialStaffAuthority10 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(downloadPlan)
                    .build();

            RoleAuthority financialStaffAuthority11 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(viewReport)
                    .build();

            RoleAuthority financialStaffAuthority12 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(downloadReport)
                    .build();

            RoleAuthority financialStaffAuthority13 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(viewAnnualReport)
                    .build();

            RoleAuthority financialStaffAuthority14 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(downloadAnnualReport)
                    .build();

            RoleAuthority financialStaffAuthority15 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(viewPlan)
                    .build();

            RoleAuthority financialStaffAuthority16 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(viewCurrency)
                    .build();

            roleAuthorityRepository.saveAll(List.of(adminAuthority1, adminAuthority2, adminAuthority3, adminAuthority4, adminAuthority5, adminAuthority6, adminAuthority7, adminAuthority8, adminAuthority9, adminAuthority10, adminAuthority14, adminAuthority15, adminAuthority16, adminAuthority17,
                    accountantAuthority1, accountantAuthority2, accountantAuthority3, accountantAuthority4, accountantAuthority5, accountantAuthority6, accountantAuthority7, accountantAuthority8, accountantAuthority9, accountantAuthority10, accountantAuthority11, accountantAuthority12, accountantAuthority13, accountantAuthority14, accountantAuthority15, accountantAuthority16, accountantAuthority17, accountantAuthority18, accountantAuthority19, accountantAuthority20, accountantAuthority21, accountantAuthority22, accountantAuthority23, accountantAuthority24, accountantAuthority25, accountantAuthority26, accountantAuthority27, accountantAuthority28, accountantAuthority29, accountantAuthority30, accountantAuthority31, accountantAuthority32, accountantAuthority33, accountantAuthority34, accountantAuthority35, accountantAuthority36,
                    financialStaffAuthority6, financialStaffAuthority7, financialStaffAuthority8, financialStaffAuthority9, financialStaffAuthority10, financialStaffAuthority11, financialStaffAuthority12, financialStaffAuthority13, financialStaffAuthority14, financialStaffAuthority15, financialStaffAuthority16
            ));

            // Report status
            ReportStatus newReportStatus = ReportStatus.builder()
                    .id(1L)
                    .name(ReportStatusCode.NEW.getValue())
                    .code(ReportStatusCode.NEW)
                    .build();

            ReportStatus waitingForApprovalReportStatus = ReportStatus.builder()
                    .id(2L)
                    .name(ReportStatusCode.WAITING_FOR_APPROVAL.getValue())
                    .code(ReportStatusCode.WAITING_FOR_APPROVAL)
                    .build();

            ReportStatus reviewedReportStatus = ReportStatus.builder()
                    .id(3L)
                    .name(ReportStatusCode.REVIEWED.getValue())
                    .code(ReportStatusCode.REVIEWED)
                    .build();


            ReportStatus closedReportStatus = ReportStatus.builder()
                    .id(4L)
                    .name(ReportStatusCode.CLOSED.getValue())
                    .code(ReportStatusCode.CLOSED)
                    .build();

            reportStatusRepository.saveAll(List.of(newReportStatus, waitingForApprovalReportStatus, reviewedReportStatus, closedReportStatus));

            Term term1 = Term.builder()
                    .id(1L)
                    .name("January 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 1, 25, 0, 0, 0).minusDays(3))
                    .endDate(LocalDateTime.of(2024, 1, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 1, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 1, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 1, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term2 = Term.builder()
                    .id(2L)
                    .name("February 2024")
                    .duration(TermDuration.QUARTERLY)
                    .startDate(LocalDateTime.of(2024, 2, 25, 0, 0, 0).minusDays(15))
                    .endDate(LocalDateTime.of(2024, 2, 25, 0, 0, 0).minusDays(15).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 2, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 2, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 2, 25, 0, 0, 0).minusDays(15)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term3 = Term.builder()
                    .id(3L)
                    .name("March 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 3, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 3, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 3, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 3, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 3, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term4 = Term.builder()
                    .id(4L)
                    .name("April 2024")
                    .duration(TermDuration.HALF_YEARLY)
                    .startDate(LocalDateTime.of(2024, 4, 25, 0, 0, 0).minusDays(2))
                    .endDate(LocalDateTime.of(2024, 4, 25, 0, 0, 0).minusDays(2).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 4, 25, 0, 0, 0).minusDays(2).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 4, 25, 0, 0, 0).minusDays(2).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 4, 25, 0, 0, 0).minusDays(2)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term5 = Term.builder()
                    .id(5L)
                    .name("May 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 5, 25, 0, 0, 0).minusDays(1))
                    .endDate(LocalDateTime.of(2024, 5, 25, 0, 0, 0).minusDays(1).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 5, 25, 0, 0, 0).minusDays(1).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 5, 25, 0, 0, 0).minusDays(1).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 5, 25, 0, 0, 0).minusDays(1)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term6 = Term.builder()
                    .id(6L)
                    .name("June 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.of(2024, 6, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 6, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 6, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 6, 25, 0, 0, 0).plusDays(5)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term7 = Term.builder()
                    .id(7L)
                    .name("July 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 7, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 7, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 7, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 7, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 7, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term8 = Term.builder()
                    .id(8L)
                    .name("August 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term9 = Term.builder()
                    .id(9L)
                    .name("September 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 9, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 9, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 9, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 9, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 9, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term10 = Term.builder()
                    .id(10L)
                    .name("October 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 10, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 10, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 10, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 10, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 10, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus)
                    .build();

            Term term11 = Term.builder()
                    .id(11L)
                    .name("November 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 11, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 11, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 11, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 11, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 11, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus)
                    .build();

            Term term12 = Term.builder()
                    .id(12L)
                    .name("December 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).minusDays(10))
                    .endDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).minusDays(10).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).minusDays(10).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).minusDays(10).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).minusDays(10)))
                    .user(user4)
                    .status(termStatus)
                    .build();

            Term term13 = Term.builder()
                    .id(13L)
                    .name("January 2025")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2025, 1, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2025, 1, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2025, 1, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2025, 1, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2025, 1, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus)
                    .build();

            Term term14 = Term.builder()
                    .id(14L)
                    .name("February 2025")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2025, 2, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2025, 2, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2025, 2, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2025, 2, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2025, 2, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus)
                    .build();

            Term term15 = Term.builder()
                    .id(15L)
                    .name("March 2025")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2025, 3, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2025, 3, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2025, 3, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2025, 3, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2025, 3, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus)
                    .build();

            Term term16 = Term.builder()
                    .id(16L)
                    .name("April 2025")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2025, 4, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2025, 4, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2025, 4, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2025, 4, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2025, 4, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus)
                    .build();

            Term term17 = Term.builder()
                    .id(17L)
                    .name("January 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 1, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 1, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 1, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 1, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 1, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term18 = Term.builder()
                    .id(18L)
                    .name("April 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 4, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 4, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 4, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 4, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 4, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term19 = Term.builder()
                    .id(19L)
                    .name("July 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.of(2023, 7, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 7, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 7, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 7, 25, 0, 0, 0).plusDays(5)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term20 = Term.builder()
                    .id(20L)
                    .name("December 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term termTester1 = Term.builder()
                    .id(21L)
                    .name("Test Term Available To Create New Plan")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term termTester2 = Term.builder()
                    .id(22L)
                    .name("Test Term Available To Re-upload Plan")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term termTester3 = Term.builder()
                    .id(23L)
                    .name("Test Term Available To Review")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            termRepository.saveAll(List.of(term1, term2, term3, term4, term5, term6, term7, term8, term9, term10, term11, term12, term13, term14, term15, term16, term17, term18, term19, term20, termTester1, termTester2, termTester3));

            FinancialPlan financialPlan1 = FinancialPlan.builder()
                    .name("Financial Plan 1")
                    .term(term1)
                    .department(itDepartment)
                    .expectedCost(BigDecimal.valueOf(1214456313L))
                    .actualCost(BigDecimal.valueOf(614456313L))
                    .build();

            FinancialPlan financialPlan2 = FinancialPlan.builder()
                    .name("Financial Plan 2")
                    .term(term2)
                    .department(itDepartment)
                    .expectedCost(BigDecimal.valueOf(914456313L))
                    .actualCost(BigDecimal.valueOf(524456313L))
                    .build();

            FinancialPlan financialPlan3 = FinancialPlan.builder()
                    .name("Financial Plan 3")
                    .term(term1)
                    .expectedCost(BigDecimal.valueOf(2214456313L))
                    .actualCost(BigDecimal.valueOf(1614456313L))
                    .department(accountingDepartment)
                    .build();

            FinancialPlan financialPlan4 = FinancialPlan.builder()
                    .name("Financial Plan 4")
                    .department(financeDepartment)
                    .term(term1)
                    .expectedCost(BigDecimal.valueOf(1214456313L))
                    .actualCost(BigDecimal.valueOf(114456313L))
                    .build();

            FinancialPlan financialPlan5 = FinancialPlan.builder()
                    .name("Financial Plan 5")
                    .term(term2)
                    .expectedCost(BigDecimal.valueOf(1514456313L))
                    .actualCost(BigDecimal.valueOf(1004456313L))
                    .department(accountingDepartment)
                    .build();

            FinancialPlan financialPlan6 = FinancialPlan.builder()
                    .name("Financial Plan 6")
                    .term(term3)
                    .expectedCost(BigDecimal.valueOf(1214456313L))
                    .actualCost(BigDecimal.valueOf(404456313L))
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan7 = FinancialPlan.builder()
                    .name("Financial Plan 7")
                    .term(term4)
                    .expectedCost(BigDecimal.valueOf(1914456313L))
                    .actualCost(BigDecimal.valueOf(1514456313L))
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan8 = FinancialPlan.builder()
                    .name("Financial Plan 8")
                    .term(term5)
                    .expectedCost(BigDecimal.valueOf(1614456313L))
                    .actualCost(BigDecimal.valueOf(1414456313L))
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan9 = FinancialPlan.builder()
                    .name("Financial Plan 9")
                    .term(term6)
                    .expectedCost(BigDecimal.valueOf(2014456313L))
                    .actualCost(BigDecimal.valueOf(154456313L))
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan10 = FinancialPlan.builder()
                    .name("Financial Plan 10")
                    .term(term7)
                    .expectedCost(BigDecimal.valueOf(1914456313L))
                    .actualCost(BigDecimal.valueOf(1004456313L))
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan11 = FinancialPlan.builder()
                    .name("Financial Plan 11")
                    .term(term8)
                    .expectedCost(BigDecimal.valueOf(1914456313L))
                    .actualCost(BigDecimal.valueOf(124456313L))
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan12 = FinancialPlan.builder()
                    .name("Financial Plan 12")
                    .term(term9)
                    .expectedCost(BigDecimal.valueOf(1214456313L))
                    .actualCost(BigDecimal.valueOf(614456313L))
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan13 = FinancialPlan.builder()
                    .name("Financial Plan 13")
                    .term(term10)
                    .expectedCost(BigDecimal.valueOf(1314456313L))
                    .actualCost(BigDecimal.valueOf(704456313L))
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan14 = FinancialPlan.builder()
                    .name("Financial Plan 14")
                    .term(term11)
                    .expectedCost(BigDecimal.valueOf(1514456313L))
                    .actualCost(BigDecimal.valueOf(914456313L))
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan15 = FinancialPlan.builder()
                    .name(term17.getName() + "_" + itDepartment.getName() + "_Plan 1")
                    .term(term17)
                    .actualCost(BigDecimal.valueOf(0))
                    .expectedCost(BigDecimal.valueOf(0))
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan16 = FinancialPlan.builder()
                    .name(term18.getName() + "_" + itDepartment.getName() + "_Plan 1")
                    .term(term18)
                    .actualCost(BigDecimal.valueOf(0))
                    .expectedCost(BigDecimal.valueOf(0))
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan17 = FinancialPlan.builder()
                    .name(term19.getName() + "_" + hrDepartment.getName() + "_Plan 1")
                    .term(term19)
                    .actualCost(BigDecimal.valueOf(0))
                    .expectedCost(BigDecimal.valueOf(0))
                    .department(hrDepartment)
                    .build();

            FinancialPlan financialPlan18 = FinancialPlan.builder()
                    .name(term20.getName() + "_" + accountant.getName() + "_Plan 1")
                    .term(term20)
                    .actualCost(BigDecimal.valueOf(0))
                    .expectedCost(BigDecimal.valueOf(0))
                    .department(accountingDepartment)
                    .build();

            FinancialPlan planTester1 = FinancialPlan.builder()
                    .name(termTester3.getName() + "_" + hrDepartment.getName() + "_Plan 1")
                    .term(termTester3)
                    .department(hrDepartment)
                    .actualCost(BigDecimal.valueOf(0))
                    .expectedCost(BigDecimal.valueOf(0))
                    .build();

            FinancialPlan planTester2 = FinancialPlan.builder()
                    .name(termTester3.getName() + "_" + accountant.getName() + "_Plan 1")
                    .term(termTester3)
                    .actualCost(BigDecimal.valueOf(0))
                    .expectedCost(BigDecimal.valueOf(0))
                    .department(accountingDepartment)
                    .build();

            planRepository.saveAll(List.of(financialPlan1, financialPlan2, financialPlan3, financialPlan4, financialPlan5, financialPlan6, financialPlan7, financialPlan8, financialPlan9, financialPlan10, financialPlan11, financialPlan12, financialPlan13, financialPlan14, financialPlan15, financialPlan16, financialPlan17, financialPlan18, planTester1, planTester2));

            CostType costType1 = CostType.builder()
                    .id(1L)
                    .name("Administration cost")
                    .build();

            CostType costType2 = CostType.builder()
                    .id(2L)
                    .name("Direct costs")
                    .build();

            CostType costType3 = CostType.builder()
                    .id(3L)
                    .name("Indirect cost")
                    .build();

            CostType costType4 = CostType.builder()
                    .id(4L)
                    .name("Operating costs")
                    .build();

            CostType costType5 = CostType.builder()
                    .id(5L)
                    .name("Maintenance costs")
                    .build();

            CostType costType6 = CostType.builder()
                    .id(6L)
                    .name("Manufacturing costs")
                    .build();

            costTypeRepository.saveAll(List.of(costType1, costType2, costType3, costType4, costType5, costType6));

            ExpenseStatus expenseStatus1 = ExpenseStatus.builder()
                    .id(1L)
                    .name("New")
                    .code(ExpenseStatusCode.NEW)
                    .build();

            ExpenseStatus expenseStatus2 = ExpenseStatus.builder()
                    .id(2L)
                    .name("Approved")
                    .code(ExpenseStatusCode.APPROVED)
                    .build();

            ExpenseStatus expenseStatus3 = ExpenseStatus.builder()
                    .id(3L)
                    .name("Denied")
                    .code(ExpenseStatusCode.DENIED)
                    .build();

            expenseStatusRepository.saveAll(List.of(expenseStatus1, expenseStatus2, expenseStatus3));

            Project project1 = Project.builder()
                    .id(1L)
                    .name("Vega Insights")
                    .build();

            Project project2 = Project.builder()
                    .id(2L)
                    .name("Nexus Analytics")
                    .build();

            Project project3 = Project.builder()
                    .id(3L)
                    .name("Orion Data")
                    .build();

            Project project4 = Project.builder()
                    .id(4L)
                    .name("Quantum Solutions")
                    .build();

            Project project5 = Project.builder()
                    .id(5L)
                    .name("Astra Metrics")
                    .build();

            Project project6 = Project.builder()
                    .id(6L)
                    .name("Luna Logic")
                    .build();

            projectRepository.saveAll(List.of(project1, project2, project3, project4, project5, project6));

            Supplier supplier1 = Supplier.builder()
                    .id(1L)
                    .name("VinaPro Co., Ltd")
                    .build();

            Supplier supplier2 = Supplier.builder()
                    .id(2L)
                    .name("SaigonTech Supply")
                    .build();

            Supplier supplier3 = Supplier.builder()
                    .id(3L)
                    .name("Hanoi Materials")
                    .build();

            Supplier supplier4 = Supplier.builder()
                    .id(4L)
                    .name("DongA Trading")
                    .build();

            Supplier supplier5 = Supplier.builder()
                    .id(5L)
                    .name("Mekong Distributors")
                    .build();

            Supplier supplier6 = Supplier.builder()
                    .id(6L)
                    .name("Lotus Supply Chain")
                    .build();

            supplierRepository.saveAll(List.of(supplier1, supplier2, supplier3, supplier4, supplier5, supplier6));

            FinancialPlanFile financialPlanFile1_1 = FinancialPlanFile.builder()
                    .id(1L)
                    .name("TERM-NAME1_PLAN-NAME1")
                    .plan(financialPlan1)
                    .user(user1)
                    .build();

            FinancialPlanFile financialPlanFile1_2 = FinancialPlanFile.builder()
                    .id(2L)
                    .name("TERM-NAME1_PLAN-NAME1")
                    .plan(financialPlan1)
                    .user(user1)
                    .build();

            FinancialPlanFile financialPlanFile2_1 = FinancialPlanFile.builder()
                    .id(3L)
                    .name("TERM-NAME2_PLAN-NAME2")
                    .plan(financialPlan2)
                    .user(user2)
                    .build();

            FinancialPlanFile financialPlanFile2_2 = FinancialPlanFile.builder()
                    .id(4L)
                    .name("TERM-NAME2_PLAN-NAME2")
                    .plan(financialPlan2)
                    .user(user2)
                    .build();

            FinancialPlanFile financialPlanFile3_1 = FinancialPlanFile.builder()
                    .id(5L)
                    .name("TERM-NAME3_PLAN-NAME3")
                    .plan(financialPlan3)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile3_2 = FinancialPlanFile.builder()
                    .id(6L)
                    .name("TERM-NAME3_PLAN-NAME3")
                    .plan(financialPlan3)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile4_1 = FinancialPlanFile.builder()
                    .id(7L)
                    .name("TERM-NAME3_PLAN-NAME4")
                    .plan(financialPlan4)
                    .user(user4)
                    .build();

            FinancialPlanFile financialPlanFile4_2 = FinancialPlanFile.builder()
                    .id(8L)
                    .name("TERM-NAME3_PLAN-NAME3")
                    .plan(financialPlan4)
                    .user(user4)
                    .build();

            FinancialPlanFile financialPlanFile5_1 = FinancialPlanFile.builder()
                    .id(9L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user4)
                    .build();

            FinancialPlanFile financialPlanFile5_2 = FinancialPlanFile.builder()
                    .id(10L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_3 = FinancialPlanFile.builder()
                    .id(11L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_4 = FinancialPlanFile.builder()
                    .id(12L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_5 = FinancialPlanFile.builder()
                    .id(13L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_6 = FinancialPlanFile.builder()
                    .id(14L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_7 = FinancialPlanFile.builder()
                    .id(15L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_8 = FinancialPlanFile.builder()
                    .id(16L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_9 = FinancialPlanFile.builder()
                    .id(17L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_10 = FinancialPlanFile.builder()
                    .id(18L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_11 = FinancialPlanFile.builder()
                    .id(19L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_12 = FinancialPlanFile.builder()
                    .id(20L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_13 = FinancialPlanFile.builder()
                    .id(21L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_14 = FinancialPlanFile.builder()
                    .id(22L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_15 = FinancialPlanFile.builder()
                    .id(23L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_16 = FinancialPlanFile.builder()
                    .id(24L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_17 = FinancialPlanFile.builder()
                    .id(25L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_18 = FinancialPlanFile.builder()
                    .id(26L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_19 = FinancialPlanFile.builder()
                    .id(27L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_20 = FinancialPlanFile.builder()
                    .id(28L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_21 = FinancialPlanFile.builder()
                    .id(29L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_22 = FinancialPlanFile.builder()
                    .id(30L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_23 = FinancialPlanFile.builder()
                    .id(31L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_24 = FinancialPlanFile.builder()
                    .id(32L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile6_1 = FinancialPlanFile.builder()
                    .id(33L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan6)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile7_1 = FinancialPlanFile.builder()
                    .id(34L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan7)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile8_1 = FinancialPlanFile.builder()
                    .id(35L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan8)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile9_1 = FinancialPlanFile.builder()
                    .id(36L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan9)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile10_1 = FinancialPlanFile.builder()
                    .id(37L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan10)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile11_1 = FinancialPlanFile.builder()
                    .id(38L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan11)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile12_1 = FinancialPlanFile.builder()
                    .id(39L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan12)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile13_1 = FinancialPlanFile.builder()
                    .id(40L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan13)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile14_1 = FinancialPlanFile.builder()
                    .id(41L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan14)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile15_1 = FinancialPlanFile.builder()
                    .name(financialPlan15.getName())
                    .plan(financialPlan15)
                    .plan(financialPlan15)
                    .user(user8)
                    .build();

            FinancialPlanFile financialPlanFile16_1 = FinancialPlanFile.builder()
                    .name(financialPlan16.getName())
                    .plan(financialPlan16)
                    .user(user6)
                    .build();

            FinancialPlanFile financialPlanFile17_1 = FinancialPlanFile.builder()
                    .name(financialPlan17.getName())
                    .plan(financialPlan17)
                    .user(user6)
                    .build();

            FinancialPlanFile financialPlanFile18_1 = FinancialPlanFile.builder()
                    .name(financialPlan18.getName())
                    .plan(financialPlan18)
                    .user(user7)
                    .build();

            FinancialPlanFile planTester1_1 = FinancialPlanFile.builder()
                    .name(planTester1.getName())
                    .plan(planTester1)
                    .user(user7)
                    .build();

            FinancialPlanFile planTester1_2 = FinancialPlanFile.builder()
                    .name(planTester1.getName())
                    .plan(planTester1)
                    .user(user6)
                    .build();

            FinancialPlanFile planTester1_3 = FinancialPlanFile.builder()
                    .name(planTester1.getName())
                    .plan(planTester1)
                    .user(user6)
                    .build();

            FinancialPlanFile planTester2_1 = FinancialPlanFile.builder()
                    .name(planTester2.getName())
                    .plan(planTester2)
                    .user(user8)
                    .build();

            FinancialPlanFile planTester2_2 = FinancialPlanFile.builder()
                    .name(planTester2.getName())
                    .plan(planTester2)
                    .user(user6)
                    .build();

            financialPlanFileRepository.saveAll(List.of(financialPlanFile1_1, financialPlanFile1_2, financialPlanFile2_1, financialPlanFile2_2, financialPlanFile3_1, financialPlanFile3_2, financialPlanFile4_1, financialPlanFile4_2, financialPlanFile5_1, financialPlanFile5_2, financialPlanFile5_3, financialPlanFile5_4, financialPlanFile5_5, financialPlanFile5_6, financialPlanFile5_7, financialPlanFile5_8, financialPlanFile5_9, financialPlanFile5_10, financialPlanFile5_11, financialPlanFile5_12, financialPlanFile5_13, financialPlanFile5_14, financialPlanFile5_15, financialPlanFile5_16, financialPlanFile5_17, financialPlanFile5_18, financialPlanFile5_19, financialPlanFile5_20, financialPlanFile5_21, financialPlanFile5_22, financialPlanFile5_23, financialPlanFile5_24, financialPlanFile6_1, financialPlanFile7_1, financialPlanFile8_1, financialPlanFile9_1, financialPlanFile10_1, financialPlanFile11_1, financialPlanFile12_1, financialPlanFile13_1, financialPlanFile14_1, financialPlanFile15_1, financialPlanFile16_1, financialPlanFile17_1, financialPlanFile18_1, planTester1_1, planTester1_2, planTester1_3, planTester2_1, planTester2_2));

            // Create currency data
            Currency vndCurrency = Currency.builder()
                    .id(1L)
                    .name("VNĐ")
                    .symbol("₫")
                    .affix(Affix.SUFFIX)
                    .isDefault(true)
                    .build();

            Currency usdCurrency = Currency.builder()
                    .id(2L)
                    .name("USD")
                    .symbol("$")
                    .affix(Affix.PREFIX)
                    .build();

            Currency jpyCurrency = Currency.builder()
                    .id(3L)
                    .name("JPY")
                    .symbol("¥")
                    .affix(Affix.PREFIX)
                    .build();

            Currency krwCurrency = Currency.builder()
                    .id(4L)
                    .name("KRW")
                    .symbol("₩")
                    .affix(Affix.PREFIX)
                    .build();

            Currency eurCurrency = Currency.builder()
                    .id(5L)
                    .name("EUR")
                    .symbol("€")
                    .affix(Affix.PREFIX)
                    .build();

            currencyRepository.saveAll(List.of(vndCurrency, usdCurrency, jpyCurrency, krwCurrency, eurCurrency));

            // Get 64 random expenses vnd and krw for plan 15, 16, 17, 18
            List<FinancialPlanExpense> expenseList = new ArrayList<>();
            Random random = new Random();
            char projectNameChar = 'A';

            for (int i = 1; i <= 128; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;
                int randomCurrencyIndex = random.nextInt(3) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    default -> expenseStatus2; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Project randomProject = switch (randomProjectIndex) {
                    case 1 -> project1;
                    case 2 -> project2;
                    case 3 -> project3;
                    case 4 -> project4;
                    case 5 -> project5;
                    case 6 -> project6;
                    default -> project1; // Default case, should never be reached
                };

                Supplier randomSupplier = switch (randomSupplierIndex) {
                    case 1 -> supplier1;
                    case 2 -> supplier2;
                    case 3 -> supplier3;
                    case 4 -> supplier4;
                    case 5 -> supplier5;
                    case 6 -> supplier6;
                    default -> supplier1; // Default case, should never be reached
                };

                User randomPic = switch (randomPicIndex) {
                    case 1 -> user6;
                    case 2 -> user7;
                    case 3 -> user8;
                    case 4 -> user9;
                    case 5 -> user10;
                    default -> user6; // Default case, should never be reached
                };

                Currency randomCurrency = switch (randomCurrencyIndex) {
                    case 1 -> vndCurrency;
                    case 2 -> krwCurrency;
                    default -> vndCurrency; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .name("Expense " + projectNameChar++)
                        .unitPrice(BigDecimal.valueOf(random.nextInt(500000) + 10000L))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .currency(randomCurrency)
                        .build();

                expenseList.add(expense);
            }

            financialPlanExpenseRepository.saveAll(expenseList);

            // Mapping expense to plan
            List<FinancialPlanFileExpense> fileExpenses = new ArrayList<>();
            for (FinancialPlanExpense expense : expenseList) {
                int randomFilePlanIndex = random.nextInt(9) + 1;
                FinancialPlanFile randomFile = switch (randomFilePlanIndex) {
                    case 1 -> financialPlanFile15_1;
                    case 2 -> financialPlanFile16_1;
                    case 3 -> financialPlanFile17_1;
                    case 4 -> financialPlanFile18_1;
                    case 5 -> planTester1_1;
                    case 6 -> planTester1_2;
                    case 7 -> planTester1_3;
                    case 8 -> planTester2_1;
                    case 9 -> planTester2_2;
                    default -> financialPlanFile15_1; // Default case, should never be reached
                };

                fileExpenses.add(FinancialPlanFileExpense.builder()
                        .file(randomFile)
                        .planExpense(expense)
                        .build());

            }

            financialPlanFileExpenseRepository.saveAll(fileExpenses);

            // Get 64 random expense usd for plan 15, 16, 17, 18
            random = new Random();
            projectNameChar = 'A';

            for (int i = 1; i <= 64; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;
                int randomCurrencyIndex = random.nextInt(4) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    default -> expenseStatus2; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Project randomProject = switch (randomProjectIndex) {
                    case 1 -> project1;
                    case 2 -> project2;
                    case 3 -> project3;
                    case 4 -> project4;
                    case 5 -> project5;
                    case 6 -> project6;
                    default -> project1; // Default case, should never be reached
                };

                Supplier randomSupplier = switch (randomSupplierIndex) {
                    case 1 -> supplier1;
                    case 2 -> supplier2;
                    case 3 -> supplier3;
                    case 4 -> supplier4;
                    case 5 -> supplier5;
                    case 6 -> supplier6;
                    default -> supplier1; // Default case, should never be reached
                };

                User randomPic = switch (randomPicIndex) {
                    case 1 -> user6;
                    case 2 -> user7;
                    case 3 -> user8;
                    case 4 -> user9;
                    case 5 -> user10;
                    default -> user6; // Default case, should never be reached
                };

                Currency randomCurrency = switch (randomCurrencyIndex) {
                    case 1 -> usdCurrency;
                    default -> usdCurrency; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .name("Expense " + projectNameChar++)
                        .unitPrice(BigDecimal.valueOf(random.nextInt(1000) + 900))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .currency(randomCurrency)
                        .build();

                expenseList.add(expense);
            }

            financialPlanExpenseRepository.saveAll(expenseList);

            // Mapping expense to plan
            fileExpenses = new ArrayList<>();
            for (FinancialPlanExpense expense : expenseList) {
                int randomFilePlanIndex = random.nextInt(9) + 1;
                FinancialPlanFile randomFile = switch (randomFilePlanIndex) {
                    case 1 -> financialPlanFile15_1;
                    case 2 -> financialPlanFile16_1;
                    case 3 -> financialPlanFile17_1;
                    case 4 -> financialPlanFile18_1;
                    case 5 -> planTester1_1;
                    case 6 -> planTester1_2;
                    case 7 -> planTester1_3;
                    case 8 -> planTester2_1;
                    case 9 -> planTester2_2;
                    default -> financialPlanFile15_1; // Default case, should never be reached
                };

                fileExpenses.add(FinancialPlanFileExpense.builder()
                        .file(randomFile)
                        .planExpense(expense)
                        .build());

            }

            financialPlanFileExpenseRepository.saveAll(fileExpenses);

            // Get 64 random expense
            expenseList = new ArrayList<>();
            random = new Random();
            projectNameChar = 'A';

            for (int i = 1; i <= 64; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;
                int randomCurrencyIndex = random.nextInt(4) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 1 -> expenseStatus1;
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    default -> expenseStatus1; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Project randomProject = switch (randomProjectIndex) {
                    case 1 -> project1;
                    case 2 -> project2;
                    case 3 -> project3;
                    case 4 -> project4;
                    case 5 -> project5;
                    case 6 -> project6;
                    default -> project1; // Default case, should never be reached
                };

                Supplier randomSupplier = switch (randomSupplierIndex) {
                    case 1 -> supplier1;
                    case 2 -> supplier2;
                    case 3 -> supplier3;
                    case 4 -> supplier4;
                    case 5 -> supplier5;
                    case 6 -> supplier6;
                    default -> supplier1; // Default case, should never be reached
                };

                User randomPic = switch (randomPicIndex) {
                    case 1 -> user1;
                    case 2 -> user2;
                    case 3 -> user3;
                    case 4 -> user4;
                    case 5 -> user5;
                    default -> user1; // Default case, should never be reached
                };

                Currency randomCurrency = switch (randomCurrencyIndex) {
                    case 1 -> vndCurrency;
                    case 2 -> usdCurrency;
                    case 3 -> jpyCurrency;
                    case 4 -> krwCurrency;
                    case 5 -> eurCurrency;
                    default -> vndCurrency; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .name("Expense " + projectNameChar++)
                        .unitPrice(BigDecimal.valueOf(random.nextInt(500000) + 10000L))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .currency(randomCurrency)
                        .build();

                expenseList.add(expense);
            }

            financialPlanExpenseRepository.saveAll(expenseList);

            FinancialPlanFileExpense fileExpense1 = FinancialPlanFileExpense.builder()
                    .id(1L)
                    .file(financialPlanFile1_1)
                    .planExpense(expenseList.get(0))
                    .build();

            FinancialPlanFileExpense fileExpense2 = FinancialPlanFileExpense.builder()
                    .id(2L)
                    .file(financialPlanFile1_2)
                    .planExpense(expenseList.get(1))
                    .build();
            FinancialPlanFileExpense fileExpense3 = FinancialPlanFileExpense.builder()
                    .id(3L)
                    .file(financialPlanFile1_2)
                    .planExpense(expenseList.get(2))
                    .build();
            FinancialPlanFileExpense fileExpense4 = FinancialPlanFileExpense.builder()
                    .id(4L)
                    .file(financialPlanFile1_2)
                    .planExpense(expenseList.get(3))
                    .build();
            FinancialPlanFileExpense fileExpense5 = FinancialPlanFileExpense.builder()
                    .id(5L)
                    .file(financialPlanFile1_2)
                    .planExpense(expenseList.get(4))
                    .build();
            FinancialPlanFileExpense fileExpense6 = FinancialPlanFileExpense.builder()
                    .id(6L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(5))
                    .build();
            FinancialPlanFileExpense fileExpense7 = FinancialPlanFileExpense.builder()
                    .id(7L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(6))
                    .build();
            FinancialPlanFileExpense fileExpense8 = FinancialPlanFileExpense.builder()
                    .id(8L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(7))
                    .build();
            FinancialPlanFileExpense fileExpense9 = FinancialPlanFileExpense.builder()
                    .id(9L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(8))
                    .build();
            FinancialPlanFileExpense fileExpense10 = FinancialPlanFileExpense.builder()
                    .id(10L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(9))
                    .build();
            FinancialPlanFileExpense fileExpense11 = FinancialPlanFileExpense.builder()
                    .id(11L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(10))
                    .build();
            FinancialPlanFileExpense fileExpense12 = FinancialPlanFileExpense.builder()
                    .id(12L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(11))
                    .build();
            FinancialPlanFileExpense fileExpense13 = FinancialPlanFileExpense.builder()
                    .id(13L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(12))
                    .build();
            FinancialPlanFileExpense fileExpense14 = FinancialPlanFileExpense.builder()
                    .id(14L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(13))
                    .build();
            FinancialPlanFileExpense fileExpense15 = FinancialPlanFileExpense.builder()
                    .id(15L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(14))
                    .build();
            FinancialPlanFileExpense fileExpense16 = FinancialPlanFileExpense.builder()
                    .id(16L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(15))
                    .build();
            FinancialPlanFileExpense fileExpense17 = FinancialPlanFileExpense.builder()
                    .id(17L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(16))
                    .build();
            FinancialPlanFileExpense fileExpense18 = FinancialPlanFileExpense.builder()
                    .id(18L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(17))
                    .build();
            FinancialPlanFileExpense fileExpense19 = FinancialPlanFileExpense.builder()
                    .id(19L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(18))
                    .build();
            FinancialPlanFileExpense fileExpense20 = FinancialPlanFileExpense.builder()
                    .id(20L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(19))
                    .build();
            FinancialPlanFileExpense fileExpense21 = FinancialPlanFileExpense.builder()
                    .id(21L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(20))
                    .build();
            FinancialPlanFileExpense fileExpense22 = FinancialPlanFileExpense.builder()
                    .id(22L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(21))
                    .build();
            FinancialPlanFileExpense fileExpense23 = FinancialPlanFileExpense.builder()
                    .id(23L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(22))
                    .build();
            FinancialPlanFileExpense fileExpense24 = FinancialPlanFileExpense.builder()
                    .id(24L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(23))
                    .build();
            FinancialPlanFileExpense fileExpense25 = FinancialPlanFileExpense.builder()
                    .id(25L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(24))
                    .build();
            FinancialPlanFileExpense fileExpense26 = FinancialPlanFileExpense.builder()
                    .id(26L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(25))
                    .build();
            FinancialPlanFileExpense fileExpense27 = FinancialPlanFileExpense.builder()
                    .id(27L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(26))
                    .build();
            FinancialPlanFileExpense fileExpense28 = FinancialPlanFileExpense.builder()
                    .id(28L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(27))
                    .build();
            FinancialPlanFileExpense fileExpense29 = FinancialPlanFileExpense.builder()
                    .id(29L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(28))
                    .build();
            FinancialPlanFileExpense fileExpense30 = FinancialPlanFileExpense.builder()
                    .id(30L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(29))
                    .build();
            FinancialPlanFileExpense fileExpense31 = FinancialPlanFileExpense.builder()
                    .id(31L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(30))
                    .build();
            FinancialPlanFileExpense fileExpense32 = FinancialPlanFileExpense.builder()
                    .id(32L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(31))
                    .build();
            FinancialPlanFileExpense fileExpense33 = FinancialPlanFileExpense.builder()
                    .id(33L)
                    .file(financialPlanFile5_3)
                    .planExpense(expenseList.get(32))
                    .build();
            FinancialPlanFileExpense fileExpense34 = FinancialPlanFileExpense.builder()
                    .id(34L)
                    .file(financialPlanFile5_4)
                    .planExpense(expenseList.get(33))
                    .build();
            FinancialPlanFileExpense fileExpense35 = FinancialPlanFileExpense.builder()
                    .id(35L)
                    .file(financialPlanFile5_5)
                    .planExpense(expenseList.get(34))
                    .build();
            FinancialPlanFileExpense fileExpense36 = FinancialPlanFileExpense.builder()
                    .id(36L)
                    .file(financialPlanFile5_6)
                    .planExpense(expenseList.get(35))
                    .build();
            FinancialPlanFileExpense fileExpense37 = FinancialPlanFileExpense.builder()
                    .id(37L)
                    .file(financialPlanFile5_7)
                    .planExpense(expenseList.get(36))
                    .build();
            FinancialPlanFileExpense fileExpense38 = FinancialPlanFileExpense.builder()
                    .id(38L)
                    .file(financialPlanFile5_8)
                    .planExpense(expenseList.get(37))
                    .build();
            FinancialPlanFileExpense fileExpense39 = FinancialPlanFileExpense.builder()
                    .id(39L)
                    .file(financialPlanFile5_9)
                    .planExpense(expenseList.get(38))
                    .build();
            FinancialPlanFileExpense fileExpense40 = FinancialPlanFileExpense.builder()
                    .id(40L)
                    .file(financialPlanFile5_10)
                    .planExpense(expenseList.get(39))
                    .build();
            FinancialPlanFileExpense fileExpense41 = FinancialPlanFileExpense.builder()
                    .id(41L)
                    .file(financialPlanFile5_11)
                    .planExpense(expenseList.get(40))
                    .build();
            FinancialPlanFileExpense fileExpense42 = FinancialPlanFileExpense.builder()
                    .id(42L)
                    .file(financialPlanFile5_12)
                    .planExpense(expenseList.get(41))
                    .build();
            FinancialPlanFileExpense fileExpense43 = FinancialPlanFileExpense.builder()
                    .id(43L)
                    .file(financialPlanFile5_13)
                    .planExpense(expenseList.get(42))
                    .build();
            FinancialPlanFileExpense fileExpense44 = FinancialPlanFileExpense.builder()
                    .id(44L)
                    .file(financialPlanFile5_14)
                    .planExpense(expenseList.get(43))
                    .build();
            FinancialPlanFileExpense fileExpense45 = FinancialPlanFileExpense.builder()
                    .id(45L)
                    .file(financialPlanFile5_15)
                    .planExpense(expenseList.get(44))
                    .build();
            FinancialPlanFileExpense fileExpense46 = FinancialPlanFileExpense.builder()
                    .id(46L)
                    .file(financialPlanFile5_16)
                    .planExpense(expenseList.get(45))
                    .build();
            FinancialPlanFileExpense fileExpense47 = FinancialPlanFileExpense.builder()
                    .id(47L)
                    .file(financialPlanFile5_17)
                    .planExpense(expenseList.get(46))
                    .build();
            FinancialPlanFileExpense fileExpense48 = FinancialPlanFileExpense.builder()
                    .id(48L)
                    .file(financialPlanFile5_18)
                    .planExpense(expenseList.get(47))
                    .build();
            FinancialPlanFileExpense fileExpense49 = FinancialPlanFileExpense.builder()
                    .id(49L)
                    .file(financialPlanFile5_19)
                    .planExpense(expenseList.get(48))
                    .build();
            FinancialPlanFileExpense fileExpense50 = FinancialPlanFileExpense.builder()
                    .id(50L)
                    .file(financialPlanFile5_20)
                    .planExpense(expenseList.get(49))
                    .build();
            FinancialPlanFileExpense fileExpense51 = FinancialPlanFileExpense.builder()
                    .id(51L)
                    .file(financialPlanFile5_21)
                    .planExpense(expenseList.get(50))
                    .build();
            FinancialPlanFileExpense fileExpense52 = FinancialPlanFileExpense.builder()
                    .id(52L)
                    .file(financialPlanFile5_22)
                    .planExpense(expenseList.get(51))
                    .build();
            FinancialPlanFileExpense fileExpense53 = FinancialPlanFileExpense.builder()
                    .id(53L)
                    .file(financialPlanFile5_23)
                    .planExpense(expenseList.get(52))
                    .build();
            FinancialPlanFileExpense fileExpense54 = FinancialPlanFileExpense.builder()
                    .id(54L)
                    .file(financialPlanFile5_24)
                    .planExpense(expenseList.get(53))
                    .build();
            FinancialPlanFileExpense fileExpense55 = FinancialPlanFileExpense.builder()
                    .id(55L)
                    .file(financialPlanFile6_1)
                    .planExpense(expenseList.get(54))
                    .build();
            FinancialPlanFileExpense fileExpense56 = FinancialPlanFileExpense.builder()
                    .id(56L)
                    .file(financialPlanFile7_1)
                    .planExpense(expenseList.get(55))
                    .build();
            FinancialPlanFileExpense fileExpense57 = FinancialPlanFileExpense.builder()
                    .id(57L)
                    .file(financialPlanFile8_1)
                    .planExpense(expenseList.get(56))
                    .build();
            FinancialPlanFileExpense fileExpense58 = FinancialPlanFileExpense.builder()
                    .id(58L)
                    .file(financialPlanFile9_1)
                    .planExpense(expenseList.get(57))
                    .build();
            FinancialPlanFileExpense fileExpense59 = FinancialPlanFileExpense.builder()
                    .id(59L)
                    .file(financialPlanFile10_1)
                    .planExpense(expenseList.get(58))
                    .build();
            FinancialPlanFileExpense fileExpense60 = FinancialPlanFileExpense.builder()
                    .id(60L)
                    .file(financialPlanFile11_1)
                    .planExpense(expenseList.get(59))
                    .build();
            FinancialPlanFileExpense fileExpense61 = FinancialPlanFileExpense.builder()
                    .id(61L)
                    .file(financialPlanFile12_1)
                    .planExpense(expenseList.get(60))
                    .build();
            FinancialPlanFileExpense fileExpense62 = FinancialPlanFileExpense.builder()
                    .id(62L)
                    .file(financialPlanFile13_1)
                    .planExpense(expenseList.get(61))
                    .build();
            FinancialPlanFileExpense fileExpense63 = FinancialPlanFileExpense.builder()
                    .id(63L)
                    .file(financialPlanFile14_1)
                    .planExpense(expenseList.get(62))
                    .build();
            FinancialPlanFileExpense fileExpense64 = FinancialPlanFileExpense.builder()
                    .id(64L)
                    .file(financialPlanFile15_1)
                    .planExpense(expenseList.get(63))
                    .build();

            financialPlanFileExpenseRepository.saveAll(List.of(fileExpense1, fileExpense2, fileExpense3, fileExpense4, fileExpense5, fileExpense6, fileExpense7, fileExpense8, fileExpense9, fileExpense10, fileExpense11, fileExpense12, fileExpense13, fileExpense14, fileExpense15, fileExpense16, fileExpense17, fileExpense18, fileExpense19, fileExpense20, fileExpense21, fileExpense22, fileExpense23, fileExpense24, fileExpense25, fileExpense26, fileExpense27, fileExpense28, fileExpense29, fileExpense30, fileExpense31, fileExpense32, fileExpense33, fileExpense34, fileExpense35, fileExpense36, fileExpense37, fileExpense38, fileExpense39, fileExpense40, fileExpense41, fileExpense42, fileExpense43, fileExpense44, fileExpense45, fileExpense46, fileExpense47, fileExpense48, fileExpense49, fileExpense50, fileExpense51, fileExpense52, fileExpense53, fileExpense54, fileExpense55, fileExpense56, fileExpense57, fileExpense58, fileExpense59, fileExpense60, fileExpense61, fileExpense62, fileExpense63, fileExpense64));

            FinancialReport report1 = FinancialReport.builder()
                    .name(term1.getName() + "_Report")
                    .month(term1.getFinalEndTermDate().toLocalDate())
                    .expectedCost(BigDecimal.valueOf(601660487L))
                    .actualCost(BigDecimal.valueOf(216579382))
                    .status(newReportStatus)
                    .term(term1)
                    .build();

            FinancialReport report2 = FinancialReport.builder()
                    .name(term2.getName() + "_Report")
                    .month(term2.getFinalEndTermDate().toLocalDate())
                    .expectedCost(BigDecimal.valueOf(901660487L))
                    .actualCost(BigDecimal.valueOf(516579382))
                    .status(waitingForApprovalReportStatus)
                    .term(term2)
                    .build();

            FinancialReport report3 = FinancialReport.builder()
                    .name(term3.getName() + "_Report")
                    .month(term3.getFinalEndTermDate().toLocalDate())
                    .expectedCost(BigDecimal.valueOf(1001660487L))
                    .actualCost(BigDecimal.valueOf(706579382))
                    .status(waitingForApprovalReportStatus)
                    .term(term3)
                    .build();

            FinancialReport report4 = FinancialReport.builder()
                    .name(term4.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(901660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(term4.getFinalEndTermDate().toLocalDate())
                    .status(reviewedReportStatus)
                    .term(term4)
                    .build();

            FinancialReport report5 = FinancialReport.builder()
                    .name(term5.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(801660487L))
                    .actualCost(BigDecimal.valueOf(585579382))
                    .month(term5.getFinalEndTermDate().toLocalDate())
                    .status(reviewedReportStatus)
                    .term(term5)
                    .build();

            FinancialReport report6 = FinancialReport.builder()
                    .name(term6.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(801660487L))
                    .actualCost(BigDecimal.valueOf(516579382))
                    .month(term6.getFinalEndTermDate().toLocalDate())
                    .status(closedReportStatus)
                    .term(term6)
                    .build();

            FinancialReport report7 = FinancialReport.builder()
                    .name(term7.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(1001660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(term7.getFinalEndTermDate().toLocalDate())
                    .status(newReportStatus)
                    .term(term7)
                    .build();

            FinancialReport report8 = FinancialReport.builder()
                    .name(term8.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(701660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(term8.getFinalEndTermDate().toLocalDate())
                    .status(waitingForApprovalReportStatus)
                    .term(term8)
                    .build();

            FinancialReport report9 = FinancialReport.builder()
                    .name(term9.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(401660487L))
                    .actualCost(BigDecimal.valueOf(316579382))
                    .month(term9.getFinalEndTermDate().toLocalDate())
                    .status(waitingForApprovalReportStatus)
                    .term(term9)
                    .build();

            FinancialReport report10 = FinancialReport.builder()
                    .name(term10.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(901660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(term10.getFinalEndTermDate().toLocalDate())
                    .status(newReportStatus)
                    .term(term10)
                    .build();

            FinancialReport report11 = FinancialReport.builder()
                    .name(term11.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(131660487L))
                    .actualCost(BigDecimal.valueOf(1216579382))
                    .month(term11.getFinalEndTermDate().toLocalDate())
                    .status(closedReportStatus)
                    .term(term11)
                    .build();

            FinancialReport report12 = FinancialReport.builder()
                    .name(term12.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(151660487L))
                    .actualCost(BigDecimal.valueOf(1006579382))
                    .month(term12.getFinalEndTermDate().toLocalDate())
                    .status(waitingForApprovalReportStatus)
                    .term(term12)
                    .build();

            FinancialReport report13 = FinancialReport.builder()
                    .name(term17.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(401660487L))
                    .actualCost(BigDecimal.valueOf(316579382))
                    .month(term17.getFinalEndTermDate().toLocalDate())
                    .status(closedReportStatus)
                    .term(term17)
                    .build();

            FinancialReport report14 = FinancialReport.builder()
                    .name(term18.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(901660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(term18.getFinalEndTermDate().toLocalDate())
                    .status(closedReportStatus)
                    .term(term18)
                    .build();

            FinancialReport report15 = FinancialReport.builder()
                    .name(term19.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(131660487L))
                    .actualCost(BigDecimal.valueOf(1216579382))
                    .month(term19.getFinalEndTermDate().toLocalDate())
                    .status(closedReportStatus)
                    .term(term19)
                    .build();

            FinancialReport report16 = FinancialReport.builder()
                    .name(term20.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(151660487L))
                    .actualCost(BigDecimal.valueOf(1006579382))
                    .month(term20.getFinalEndTermDate().toLocalDate())
                    .status(closedReportStatus)
                    .term(term20)
                    .build();

            FinancialReport reportTester1 = FinancialReport.builder()
                    .name(termTester1.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(901660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(termTester1.getFinalEndTermDate().toLocalDate())
                    .status(newReportStatus)
                    .term(termTester1)
                    .build();

            FinancialReport reportTester2 = FinancialReport.builder()
                    .name(termTester2.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(131660487L))
                    .actualCost(BigDecimal.valueOf(1216579382))
                    .month(termTester2.getFinalEndTermDate().toLocalDate())
                    .status(reviewedReportStatus)
                    .term(termTester2)
                    .build();

            FinancialReport reportTester3 = FinancialReport.builder()
                    .name(termTester3.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(151660487L))
                    .actualCost(BigDecimal.valueOf(1006579382))
                    .month(termTester3.getFinalEndTermDate().toLocalDate())
                    .status(reviewedReportStatus)
                    .term(termTester3)
                    .build();

            financialReportRepository.saveAll(List.of(report1, report2, report3, report4, report5, report6, report7, report8, report9, report10, report11, report12, report13, report14, report15, report16, reportTester1, reportTester2, reportTester3));

            // Get 15 random expense
//            List<FinancialReportExpense> reportExpenseList = new ArrayList<>();
//            projectNameChar = 'A';
//            supplierNameChar = 'A';
//
//            for (int i = 1; i <= 15; i++) {
//                int randomStatusIndex = random.nextInt(4) + 1;
//                int randomCostTypeIndex = random.nextInt(6) + 1;
//                int randomReportIndex = random.nextInt(6) + 1;
//                int randomPicIndex = random.nextInt(pics.length);
//
//                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
//                    case 3 -> expenseStatus3;
//                    case 4 -> expenseStatus4;
//                    default -> expenseStatus3; // Default case, should never be reached
//                };
//
//                CostType randomCostType = switch (randomCostTypeIndex) {
//                    case 1 -> costType1;
//                    case 2 -> costType2;
//                    case 3 -> costType3;
//                    case 4 -> costType4;
//                    case 5 -> costType5;
//                    case 6 -> costType6;
//                    default -> costType1; // Default case, should never be reached
//                };
//
//                FinancialReport randomReport = switch (randomReportIndex) {
//                    case 1 -> report1;
//                    case 2 -> report2;
//                    case 3 -> report3;
//                    case 4 -> report4;
//                    case 5 -> report5;
//                    case 6 -> report6;
//                    default -> report1; // Default case, should never be reached
//                };
//
//                FinancialReportExpense expense = FinancialReportExpense.builder()
//                        .name("Expense " + projectNameChar)
//                        .unitPrice(BigDecimal.valueOf(random.nextInt(5000000) + 1000000))
//                        .amount(random.nextInt(10) + 1)
//                        .financialReport(randomReport)
//                        .projectName("Project name " + projectNameChar++)
//                        .supplierName("Supplier name " + supplierNameChar++)
//                        .pic(pics[randomPicIndex])
//                        .costType(randomCostType)
//                        .status(randomExpenseStatus)
//                        .build();
//
//                reportExpenseList.add(expense);
//            }
//
//            financialReportExpenseRepository.saveAll(reportExpenseList);


            AnnualReport annualReport1 = AnnualReport.builder()
                    .year(2020)
                    .totalTerm(12)
                    .totalExpense(BigDecimal.valueOf(1232212125))
                    .totalDepartment(6)
                    .build();

            AnnualReport annualReport2 = AnnualReport.builder()
                    .year(2021)
                    .totalTerm(12)
                    .totalExpense(BigDecimal.valueOf(461321564))
                    .totalDepartment(5)
                    .build();

            AnnualReport annualReport3 = AnnualReport.builder()
                    .year(2022)
                    .totalTerm(22)
                    .totalExpense(BigDecimal.valueOf(1231313213))
                    .totalDepartment(3)
                    .build();

            AnnualReport annualReport4 = AnnualReport.builder()
                    .year(2023)
                    .totalTerm(12)
                    .totalExpense(BigDecimal.valueOf(905135545))
                    .totalDepartment(6)
                    .build();

            AnnualReport annualReport5 = AnnualReport.builder()
                    .year(2024)
                    .totalTerm(9)
                    .totalExpense(BigDecimal.valueOf(843513112))
                    .totalDepartment(5)
                    .build();

            AnnualReport annualReport6 = AnnualReport.builder()
                    .year(2025)
                    .totalTerm(6)
                    .totalExpense(BigDecimal.valueOf(354564895))
                    .totalDepartment(4)
                    .build();

            annualReportRepository.saveAll(List.of(annualReport1, annualReport2, annualReport3, annualReport4, annualReport5, annualReport6));

            List<Report> reports = new ArrayList<>();
            random = new Random();
            projectNameChar = 'A';

            for (int i = 1; i <= 30; i++) {
                int randomAnnualReportIndex = random.nextInt(6) + 1;
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomDepartmentIndex = random.nextInt(6) + 1;

                AnnualReport randomAnnualReport = switch (randomAnnualReportIndex) {
                    case 1 -> annualReport1;
                    case 2 -> annualReport2;
                    case 3 -> annualReport3;
                    case 4 -> annualReport4;
                    case 6 -> annualReport6;
                    default -> annualReport1; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Department randomDepartment = switch (randomDepartmentIndex) {
                    case 1 -> accountingDepartment;
                    case 2 -> financeDepartment;
                    case 3 -> itDepartment;
                    case 4 -> communicationDepartment;
                    case 5 -> hrDepartment;
                    case 6 -> marketingDepartment;
                    default -> accountingDepartment; // Default case, should never be reached
                };

                Report report = Report.builder()
                        .totalExpense(BigDecimal.valueOf(random.nextInt(5000000) + 2000000))
                        .biggestExpenditure(BigDecimal.valueOf(random.nextInt(1500000) + 100000))
                        .annualReport(randomAnnualReport)
                        .department(randomDepartment)
                        .costType(randomCostType)
                        .build();

                reports.add(report);
            }

            reportRepository.saveAll(reports);

            CurrencyExchangeRate exchangeRate1_1 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 4, 17))
                    .currency(vndCurrency)
                    .amount(BigDecimal.valueOf(1))
                    .build();

            CurrencyExchangeRate exchangeRate1_2 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 4, 10))
                    .currency(usdCurrency)
                    .amount(BigDecimal.valueOf(22000))
                    .build();

            CurrencyExchangeRate exchangeRate1_3 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 4, 11))
                    .currency(jpyCurrency)
                    .amount(BigDecimal.valueOf(171))
                    .build();

            CurrencyExchangeRate exchangeRate1_4 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 4, 18))
                    .currency(krwCurrency)
                    .amount(BigDecimal.valueOf(18))
                    .build();

            CurrencyExchangeRate exchangeRate1_5 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 4, 18))
                    .currency(eurCurrency)
                    .amount(BigDecimal.valueOf(27636))
                    .build();

            CurrencyExchangeRate exchangeRate2_1 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 5, 17))
                    .currency(vndCurrency)
                    .amount(BigDecimal.valueOf(1))
                    .build();

            CurrencyExchangeRate exchangeRate2_2 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 5, 18))
                    .currency(usdCurrency)
                    .amount(BigDecimal.valueOf(23500))
                    .build();

            CurrencyExchangeRate exchangeRate2_3 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 5, 20))
                    .currency(jpyCurrency)
                    .amount(BigDecimal.valueOf(153))
                    .build();

            CurrencyExchangeRate exchangeRate2_4 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 5, 25))
                    .currency(krwCurrency)
                    .amount(BigDecimal.valueOf(19))
                    .build();

            CurrencyExchangeRate exchangeRate2_5 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 5, 25))
                    .currency(eurCurrency)
                    .amount(BigDecimal.valueOf(27000))
                    .build();

            CurrencyExchangeRate exchangeRate3_1 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 6, 17))
                    .currency(vndCurrency)
                    .amount(BigDecimal.valueOf(1))
                    .build();

            CurrencyExchangeRate exchangeRate3_2 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 6, 18))
                    .currency(usdCurrency)
                    .amount(BigDecimal.valueOf(23800))
                    .build();

            CurrencyExchangeRate exchangeRate3_3 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 6, 20))
                    .currency(jpyCurrency)
                    .amount(BigDecimal.valueOf(162))
                    .build();

            CurrencyExchangeRate exchangeRate3_4 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 6, 25))
                    .currency(krwCurrency)
                    .amount(BigDecimal.valueOf(22))
                    .build();

            CurrencyExchangeRate exchangeRate3_5 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 6, 25))
                    .currency(eurCurrency)
                    .amount(BigDecimal.valueOf(25000))
                    .build();

            CurrencyExchangeRate exchangeRate4_1 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 7, 17))
                    .currency(vndCurrency)
                    .amount(BigDecimal.valueOf(1))
                    .build();

            CurrencyExchangeRate exchangeRate4_2 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 7, 18))
                    .currency(usdCurrency)
                    .amount(BigDecimal.valueOf(21500))
                    .build();

            CurrencyExchangeRate exchangeRate4_3 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 7, 20))
                    .currency(jpyCurrency)
                    .amount(BigDecimal.valueOf(162))
                    .build();

            CurrencyExchangeRate exchangeRate4_4 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 7, 25))
                    .currency(krwCurrency)
                    .amount(BigDecimal.valueOf(18.6))
                    .build();

            CurrencyExchangeRate exchangeRate4_5 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 7, 25))
                    .currency(krwCurrency)
                    .amount(BigDecimal.valueOf(25500))
                    .build();

            CurrencyExchangeRate exchangeRate5_1 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 8, 17))
                    .currency(vndCurrency)
                    .amount(BigDecimal.valueOf(1))
                    .build();

            CurrencyExchangeRate exchangeRate5_2 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 8, 18))
                    .currency(usdCurrency)
                    .amount(BigDecimal.valueOf(20000))
                    .build();

            CurrencyExchangeRate exchangeRate5_3 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 8, 20))
                    .currency(jpyCurrency)
                    .amount(BigDecimal.valueOf(162))
                    .build();

            CurrencyExchangeRate exchangeRate5_4 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 8, 25))
                    .currency(krwCurrency)
                    .amount(BigDecimal.valueOf(15.8))
                    .build();

            CurrencyExchangeRate exchangeRate5_5 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 8, 25))
                    .currency(eurCurrency)
                    .amount(BigDecimal.valueOf(24500))
                    .build();

            CurrencyExchangeRate exchangeRate6_1 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 9, 17))
                    .currency(vndCurrency)
                    .amount(BigDecimal.valueOf(1))
                    .build();

            CurrencyExchangeRate exchangeRate6_2 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 9, 18))
                    .currency(usdCurrency)
                    .amount(BigDecimal.valueOf(24500))
                    .build();

            CurrencyExchangeRate exchangeRate6_3 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 9, 20))
                    .currency(jpyCurrency)
                    .amount(BigDecimal.valueOf(133))
                    .build();

            CurrencyExchangeRate exchangeRate6_4 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 9, 25))
                    .currency(krwCurrency)
                    .amount(BigDecimal.valueOf(1550))
                    .build();

            CurrencyExchangeRate exchangeRate6_5 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 9, 25))
                    .currency(krwCurrency)
                    .amount(BigDecimal.valueOf(1550))
                    .build();

            for (int i = 0; i < 12; i++) {
                CurrencyExchangeRate exchangeRate1 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2024, i + 1, 23))
                        .currency(vndCurrency)
                        .amount(BigDecimal.valueOf(1))
                        .build();

                CurrencyExchangeRate exchangeRate2 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2024, i + 1, 23))
                        .currency(usdCurrency)
                        .amount(BigDecimal.valueOf(25000))
                        .build();

                CurrencyExchangeRate exchangeRate3 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2024, i + 1, 23))
                        .currency(jpyCurrency)
                        .amount(BigDecimal.valueOf(171))
                        .build();

                CurrencyExchangeRate exchangeRate4 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2024, i + 1, 23))
                        .currency(krwCurrency)
                        .amount(BigDecimal.valueOf(16))
                        .build();

                CurrencyExchangeRate exchangeRate5 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2024, i + 1, 23))
                        .currency(eurCurrency)
                        .amount(BigDecimal.valueOf(27000))
                        .build();

                currencyExchangeRateRepository.saveAll(List.of(exchangeRate1, exchangeRate2, exchangeRate3, exchangeRate4, exchangeRate5));
            }

            for (int i = 0; i < 12; i++) {
                CurrencyExchangeRate exchangeRate1 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2023, i + 1, 23))
                        .currency(vndCurrency)
                        .amount(BigDecimal.valueOf(1))
                        .build();

                CurrencyExchangeRate exchangeRate2 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2023, i + 1, 23))
                        .currency(usdCurrency)
                        .amount(BigDecimal.valueOf(23000))
                        .build();

                CurrencyExchangeRate exchangeRate3 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2023, i + 1, 23))
                        .currency(jpyCurrency)
                        .amount(BigDecimal.valueOf(171))
                        .build();

                CurrencyExchangeRate exchangeRate4 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2023, i + 1, 23))
                        .currency(krwCurrency)
                        .amount(BigDecimal.valueOf(16.4))
                        .build();

                CurrencyExchangeRate exchangeRate5 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2023, i + 1, 23))
                        .currency(eurCurrency)
                        .amount(BigDecimal.valueOf(27650))
                        .build();

                currencyExchangeRateRepository.saveAll(List.of(exchangeRate1, exchangeRate2, exchangeRate3, exchangeRate4, exchangeRate5));
            }

            currencyExchangeRateRepository.saveAll(List.of(exchangeRate1_1, exchangeRate1_2, exchangeRate1_3, exchangeRate1_4, exchangeRate1_5, exchangeRate2_1, exchangeRate2_2, exchangeRate2_3, exchangeRate2_4, exchangeRate2_5, exchangeRate3_1, exchangeRate3_2, exchangeRate3_3, exchangeRate3_4, exchangeRate3_5, exchangeRate4_1, exchangeRate4_2, exchangeRate4_3, exchangeRate4_4, exchangeRate4_5, exchangeRate5_1, exchangeRate5_2, exchangeRate5_3, exchangeRate5_4, exchangeRate5_5, exchangeRate6_1, exchangeRate6_2, exchangeRate6_3, exchangeRate6_4, exchangeRate6_5));


            List<ReportStatistical> statistics = new ArrayList<>();
            random = new Random();
            projectNameChar = 'A';

            for (int i = 1; i <= 128; i++) {
                int randomAnnualReportIndex = random.nextInt(16) + 1;
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomDepartmentIndex = random.nextInt(6) + 1;

                FinancialReport randomReport = switch (randomAnnualReportIndex) {
                    case 1 -> report1;
                    case 2 -> report2;
                    case 3 -> report3;
                    case 4 -> report4;
                    case 5 -> report5;
                    case 6 -> report6;
                    case 7 -> report7;
                    case 8 -> report8;
                    case 9 -> report9;
                    case 10 -> report10;
                    case 11 -> report11;
                    case 12 -> report12;
                    case 13 -> report13;
                    case 14 -> report14;
                    case 15 -> report15;
                    case 16 -> report16;
                    default -> report1; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Department randomDepartment = switch (randomDepartmentIndex) {
                    case 1 -> accountingDepartment;
                    case 2 -> financeDepartment;
                    case 3 -> itDepartment;
                    case 4 -> communicationDepartment;
                    case 5 -> hrDepartment;
                    case 6 -> marketingDepartment;
                    default -> accountingDepartment; // Default case, should never be reached
                };

                ReportStatistical report = ReportStatistical.builder()
                        .totalExpense(BigDecimal.valueOf(random.nextInt(5000000) + 2000000))
                        .biggestExpenditure(BigDecimal.valueOf(random.nextInt(1500000) + 100000))
                        .report(randomReport)
                        .department(randomDepartment)
                        .costType(randomCostType)
                        .build();

                statistics.add(report);
            }

            reportStatisticalRepository.saveAll(statistics);
        };
    }
}
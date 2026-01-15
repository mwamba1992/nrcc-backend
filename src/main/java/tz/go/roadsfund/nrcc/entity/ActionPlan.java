package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;
import tz.go.roadsfund.nrcc.enums.ActionPlanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Action Plan entity for NRCC annual planning
 */
@Entity
@Table(name = "action_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "financial_year", nullable = false, length = 20)
    private String financialYear;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 20)
    private String version = "1.0";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ActionPlanStatus status = ActionPlanStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prepared_by_id")
    private User preparedBy;

    @Column(name = "prepared_date")
    private LocalDate preparedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_id")
    private User approvedBy;

    @Column(name = "approved_date")
    private LocalDate approvedDate;

    @Column(name = "approval_resolution", columnDefinition = "TEXT")
    private String approvalResolution;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "actionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ActionPlanTarget> targets = new ArrayList<>();

    @Column(name = "total_budget", precision = 15, scale = 2)
    private BigDecimal totalBudget;
}

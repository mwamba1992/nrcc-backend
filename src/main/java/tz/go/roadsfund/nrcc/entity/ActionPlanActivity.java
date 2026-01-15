package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;
import tz.go.roadsfund.nrcc.enums.ActivityStatus;
import tz.go.roadsfund.nrcc.enums.Quarter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Action Plan Activity entity
 */
@Entity
@Table(name = "action_plan_activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionPlanActivity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private ActionPlanTarget target;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ElementCollection(targetClass = Quarter.class)
    @CollectionTable(name = "activity_quarter_schedule", joinColumns = @JoinColumn(name = "activity_id"))
    @Column(name = "quarter")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Quarter> quarterSchedule = new HashSet<>();

    @Column(name = "expected_output", columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(name = "responsible_unit", length = 255)
    private String responsibleUnit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ActivityStatus status = ActivityStatus.NOT_STARTED;

    @Column(name = "progress_percent")
    private Integer progressPercent = 0;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "actual_cost", precision = 15, scale = 2)
    private BigDecimal actualCost;

    @Column(name = "display_order")
    private Integer displayOrder;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ActionPlanCostItem> costItems = new ArrayList<>();
}

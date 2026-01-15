package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Action Plan Target entity
 */
@Entity
@Table(name = "action_plan_targets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionPlanTarget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_plan_id", nullable = false)
    private ActionPlan actionPlan;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(length = 255)
    private String indicator;

    @Column(length = 255)
    private String baseline;

    @Column(name = "target_value", length = 255)
    private String targetValue;

    @Column(name = "display_order")
    private Integer displayOrder;

    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ActionPlanActivity> activities = new ArrayList<>();

    @Column(name = "subtotal", precision = 15, scale = 2)
    private BigDecimal subtotal;
}

package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Action Plan Cost Item entity
 */
@Entity
@Table(name = "action_plan_cost_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionPlanCostItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private ActionPlanActivity activity;

    @Column(name = "cost_item", nullable = false, length = 500)
    private String costItem;

    @Column(length = 100)
    private String unit;

    @Column(precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(precision = 15, scale = 2)
    private BigDecimal rate;

    @Column(name = "computed_cost", precision = 15, scale = 2)
    private BigDecimal computedCost;

    @PrePersist
    @PreUpdate
    private void calculateCost() {
        if (quantity != null && rate != null) {
            this.computedCost = quantity.multiply(rate);
        }
    }
}

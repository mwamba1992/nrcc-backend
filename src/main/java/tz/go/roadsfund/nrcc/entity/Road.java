package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;
import tz.go.roadsfund.nrcc.enums.RoadClass;

import java.math.BigDecimal;

/**
 * Road entity
 */
@Entity
@Table(name = "roads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Road extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "road_number", length = 50)
    private String roadNumber;

    @Column(precision = 10, scale = 2)
    private BigDecimal length;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_class", nullable = false, length = 20)
    private RoadClass currentClass;

    @Column(name = "start_point", length = 500)
    private String startPoint;

    @Column(name = "end_point", length = 500)
    private String endPoint;

    @Column(length = 100)
    private String region;

    @Column(length = 100)
    private String district;

    @Column(name = "surface_type", length = 100)
    private String surfaceType;

    @Column(name = "carriageway_width", precision = 8, scale = 2)
    private BigDecimal carriagewayWidth;

    @Column(name = "formation_width", precision = 8, scale = 2)
    private BigDecimal formationWidth;

    @Column(name = "road_reserve_width", precision = 8, scale = 2)
    private BigDecimal roadReserveWidth;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20)
    private String status = "ACTIVE";
}

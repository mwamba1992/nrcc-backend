package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Application Form Data entity (Fourth Schedule fields)
 */
@Entity
@Table(name = "application_form_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationFormData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "road_name", nullable = false, length = 255)
    private String roadName;

    @Column(name = "road_length", precision = 10, scale = 2)
    private BigDecimal roadLength;

    @Column(name = "current_class", length = 50)
    private String currentClass;

    @Column(name = "proposed_class", length = 50)
    private String proposedClass;

    @Column(name = "starting_point", columnDefinition = "TEXT")
    private String startingPoint;

    @Column(name = "terminal_point", columnDefinition = "TEXT")
    private String terminalPoint;

    @Column(name = "reclassification_reasons", columnDefinition = "TEXT")
    private String reclassificationReasons;

    @Column(name = "surface_type_carriageway", length = 255)
    private String surfaceTypeCarriageway;

    @Column(name = "surface_type_shoulders", length = 255)
    private String surfaceTypeShoulders;

    @Column(name = "carriageway_width", precision = 8, scale = 2)
    private BigDecimal carriagewayWidth;

    @Column(name = "formation_width", precision = 8, scale = 2)
    private BigDecimal formationWidth;

    @Column(name = "actual_road_reserve_width", precision = 8, scale = 2)
    private BigDecimal actualRoadReserveWidth;

    @Column(name = "traffic_level", columnDefinition = "TEXT")
    private String trafficLevel;

    @Column(name = "traffic_composition", columnDefinition = "TEXT")
    private String trafficComposition;

    @Column(name = "towns_villages_linked", columnDefinition = "TEXT")
    private String townsVillagesLinked;

    @Column(name = "principal_nodes", columnDefinition = "TEXT")
    private String principalNodes;

    @Column(name = "bus_routes", columnDefinition = "TEXT")
    private String busRoutes;

    @Column(name = "public_services", columnDefinition = "TEXT")
    private String publicServices;

    @Column(name = "alternative_routes", columnDefinition = "TEXT")
    private String alternativeRoutes;

    @Column(name = "map_sketch_path", length = 500)
    private String mapSketchPath;

    @Column(columnDefinition = "TEXT")
    private String attachments;
}

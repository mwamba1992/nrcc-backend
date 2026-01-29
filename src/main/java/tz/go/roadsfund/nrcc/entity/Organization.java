package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Organization entity
 */
@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "organization_type", length = 100)
    private String organizationType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "contact_person", length = 255)
    private String contactPerson;

    @Column(length = 255)
    private String email;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @Column(length = 20)
    @Builder.Default
    private String status = "ACTIVE";
}

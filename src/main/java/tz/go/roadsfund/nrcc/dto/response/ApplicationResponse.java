package tz.go.roadsfund.nrcc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Basic application response DTO for list views
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {

    private Long id;
    private String applicationNumber;
    private String applicantType;
    private String applicantName;
    private String applicantEmail;
    private String roadName;
    private String currentClass;
    private String proposedClass;
    private String status;
    private String statusDisplayName;
    private String currentOwnerName;
    private LocalDate submissionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

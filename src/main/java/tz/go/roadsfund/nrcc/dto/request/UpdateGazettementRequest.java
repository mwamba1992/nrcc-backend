package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for updating gazettement status
 */
@Data
public class UpdateGazettementRequest {

    @NotBlank(message = "Gazette number is required")
    private String gazetteNumber;

    @NotNull(message = "Gazette date is required")
    private LocalDate gazetteDate;
}

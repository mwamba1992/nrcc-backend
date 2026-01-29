package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * DTO for bulk user operations
 */
@Data
public class BulkUserActionRequest {

    @NotEmpty(message = "User IDs are required")
    private List<Long> userIds;

    @NotNull(message = "Action is required")
    private BulkAction action;

    public enum BulkAction {
        ACTIVATE,
        DEACTIVATE,
        DELETE
    }
}

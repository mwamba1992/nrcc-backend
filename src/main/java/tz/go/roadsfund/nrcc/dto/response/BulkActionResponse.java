package tz.go.roadsfund.nrcc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for bulk operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkActionResponse {

    private int totalRequested;
    private int successCount;
    private int failedCount;
    private List<Long> successIds;
    private List<FailedItem> failedItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FailedItem {
        private Long userId;
        private String reason;
    }
}

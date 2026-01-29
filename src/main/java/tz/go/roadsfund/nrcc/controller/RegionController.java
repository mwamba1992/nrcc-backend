package tz.go.roadsfund.nrcc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.go.roadsfund.nrcc.dto.request.CreateRegionRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateRegionRequest;
import tz.go.roadsfund.nrcc.dto.response.ApiResponse;
import tz.go.roadsfund.nrcc.dto.response.RegionResponse;
import tz.go.roadsfund.nrcc.enums.Permission;
import tz.go.roadsfund.nrcc.security.RequirePermission;
import tz.go.roadsfund.nrcc.service.RegionService;

import java.util.List;

/**
 * REST Controller for region management operations
 */
@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    // ==================== CREATE ====================

    /**
     * Create a new region
     */
    @PostMapping
    @RequirePermission(Permission.SYSTEM_CONFIGURE)
    public ResponseEntity<ApiResponse<RegionResponse>> createRegion(
            @Valid @RequestBody CreateRegionRequest request) {
        RegionResponse region = regionService.createRegion(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Region created successfully", region));
    }

    // ==================== READ ====================

    /**
     * Get all regions
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RegionResponse>>> getAllRegions(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        List<RegionResponse> regions = activeOnly
                ? regionService.getActiveRegions()
                : regionService.getAllRegions();
        return ResponseEntity.ok(ApiResponse.success("Regions retrieved successfully", regions));
    }

    /**
     * Get regions with pagination
     */
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<RegionResponse>>> getRegionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<RegionResponse> regions = regionService.getRegionsPaginated(pageable);
        return ResponseEntity.ok(ApiResponse.success("Regions retrieved successfully", regions));
    }

    /**
     * Get region by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RegionResponse>> getRegionById(@PathVariable Long id) {
        RegionResponse region = regionService.getRegionById(id);
        return ResponseEntity.ok(ApiResponse.success("Region retrieved successfully", region));
    }

    /**
     * Get region by code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<RegionResponse>> getRegionByCode(@PathVariable String code) {
        RegionResponse region = regionService.getRegionByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Region retrieved successfully", region));
    }

    /**
     * Search regions by name
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<RegionResponse>>> searchRegions(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<RegionResponse> regions = regionService.searchRegionsByName(name, pageable);
        return ResponseEntity.ok(ApiResponse.success("Regions retrieved successfully", regions));
    }

    // ==================== UPDATE ====================

    /**
     * Update region
     */
    @PutMapping("/{id}")
    @RequirePermission(Permission.SYSTEM_CONFIGURE)
    public ResponseEntity<ApiResponse<RegionResponse>> updateRegion(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRegionRequest request) {
        RegionResponse region = regionService.updateRegion(id, request);
        return ResponseEntity.ok(ApiResponse.success("Region updated successfully", region));
    }

    // ==================== DELETE ====================

    /**
     * Delete region
     */
    @DeleteMapping("/{id}")
    @RequirePermission(Permission.SYSTEM_CONFIGURE)
    public ResponseEntity<ApiResponse<Void>> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.ok(ApiResponse.success("Region deleted successfully", null));
    }

    // ==================== STATUS MANAGEMENT ====================

    /**
     * Activate region
     */
    @PutMapping("/{id}/activate")
    @RequirePermission(Permission.SYSTEM_CONFIGURE)
    public ResponseEntity<ApiResponse<Void>> activateRegion(@PathVariable Long id) {
        regionService.activateRegion(id);
        return ResponseEntity.ok(ApiResponse.success("Region activated successfully", null));
    }

    /**
     * Deactivate region
     */
    @PutMapping("/{id}/deactivate")
    @RequirePermission(Permission.SYSTEM_CONFIGURE)
    public ResponseEntity<ApiResponse<Void>> deactivateRegion(@PathVariable Long id) {
        regionService.deactivateRegion(id);
        return ResponseEntity.ok(ApiResponse.success("Region deactivated successfully", null));
    }

    // ==================== STATISTICS ====================

    /**
     * Get region statistics
     */
    @GetMapping("/stats/count")
    public ResponseEntity<ApiResponse<RegionStats>> getRegionStats() {
        long total = regionService.countAllRegions();
        long active = regionService.countActiveRegions();

        RegionStats stats = new RegionStats(total, active, total - active);
        return ResponseEntity.ok(ApiResponse.success("Region statistics retrieved", stats));
    }

    /**
     * Inner class for region statistics
     */
    public record RegionStats(long total, long active, long inactive) {}
}

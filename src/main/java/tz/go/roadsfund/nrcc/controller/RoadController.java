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
import tz.go.roadsfund.nrcc.dto.request.CreateRoadRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateRoadRequest;
import tz.go.roadsfund.nrcc.dto.response.ApiResponse;
import tz.go.roadsfund.nrcc.dto.response.RoadResponse;
import tz.go.roadsfund.nrcc.enums.Permission;
import tz.go.roadsfund.nrcc.security.RequirePermission;
import tz.go.roadsfund.nrcc.service.RoadService;

import java.util.List;

/**
 * REST Controller for road management
 */
@RestController
@RequestMapping("/roads")
@RequiredArgsConstructor
public class RoadController {

    private final RoadService roadService;

    /**
     * Create a new road
     */
    @PostMapping
    @RequirePermission(Permission.ROAD_CREATE)
    public ResponseEntity<ApiResponse<RoadResponse>> createRoad(@Valid @RequestBody CreateRoadRequest request) {
        RoadResponse road = roadService.createRoad(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Road created successfully", road));
    }

    /**
     * Get road by ID
     */
    @GetMapping("/{id}")
    @RequirePermission(anyOf = {Permission.ROAD_READ, Permission.ROAD_LIST})
    public ResponseEntity<ApiResponse<RoadResponse>> getRoadById(@PathVariable Long id) {
        RoadResponse road = roadService.getRoadById(id);
        return ResponseEntity.ok(ApiResponse.success("Road retrieved successfully", road));
    }

    /**
     * Get road by road number
     */
    @GetMapping("/number/{roadNumber}")
    @RequirePermission(anyOf = {Permission.ROAD_READ, Permission.ROAD_LIST})
    public ResponseEntity<ApiResponse<RoadResponse>> getRoadByNumber(@PathVariable String roadNumber) {
        RoadResponse road = roadService.getRoadByNumber(roadNumber);
        return ResponseEntity.ok(ApiResponse.success("Road retrieved successfully", road));
    }

    /**
     * Get all roads
     */
    @GetMapping
    @RequirePermission(Permission.ROAD_LIST)
    public ResponseEntity<ApiResponse<List<RoadResponse>>> getAllRoads() {
        List<RoadResponse> roads = roadService.getAllRoads();
        return ResponseEntity.ok(ApiResponse.success("Roads retrieved successfully", roads));
    }

    /**
     * Get roads with pagination
     */
    @GetMapping("/paginated")
    @RequirePermission(Permission.ROAD_LIST)
    public ResponseEntity<ApiResponse<Page<RoadResponse>>> getRoadsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<RoadResponse> roads = roadService.getRoadsPaginated(pageable);
        return ResponseEntity.ok(ApiResponse.success("Roads retrieved successfully", roads));
    }

    /**
     * Get roads by region
     */
    @GetMapping("/region/{region}")
    @RequirePermission(Permission.ROAD_LIST)
    public ResponseEntity<ApiResponse<List<RoadResponse>>> getRoadsByRegion(@PathVariable String region) {
        List<RoadResponse> roads = roadService.getRoadsByRegion(region);
        return ResponseEntity.ok(ApiResponse.success("Roads retrieved successfully", roads));
    }

    /**
     * Get roads by district
     */
    @GetMapping("/district/{district}")
    @RequirePermission(Permission.ROAD_LIST)
    public ResponseEntity<ApiResponse<List<RoadResponse>>> getRoadsByDistrict(@PathVariable String district) {
        List<RoadResponse> roads = roadService.getRoadsByDistrict(district);
        return ResponseEntity.ok(ApiResponse.success("Roads retrieved successfully", roads));
    }

    /**
     * Get roads by status
     */
    @GetMapping("/status/{status}")
    @RequirePermission(Permission.ROAD_LIST)
    public ResponseEntity<ApiResponse<List<RoadResponse>>> getRoadsByStatus(@PathVariable String status) {
        List<RoadResponse> roads = roadService.getRoadsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Roads retrieved successfully", roads));
    }

    /**
     * Update road
     */
    @PutMapping("/{id}")
    @RequirePermission(Permission.ROAD_UPDATE)
    public ResponseEntity<ApiResponse<RoadResponse>> updateRoad(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoadRequest request) {
        RoadResponse road = roadService.updateRoad(id, request);
        return ResponseEntity.ok(ApiResponse.success("Road updated successfully", road));
    }

    /**
     * Delete road
     */
    @DeleteMapping("/{id}")
    @RequirePermission(Permission.ROAD_DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteRoad(@PathVariable Long id) {
        roadService.deleteRoad(id);
        return ResponseEntity.ok(ApiResponse.success("Road deleted successfully", null));
    }

    /**
     * Activate road
     */
    @PutMapping("/{id}/activate")
    @RequirePermission(Permission.ROAD_UPDATE)
    public ResponseEntity<ApiResponse<Void>> activateRoad(@PathVariable Long id) {
        roadService.activateRoad(id);
        return ResponseEntity.ok(ApiResponse.success("Road activated successfully", null));
    }

    /**
     * Deactivate road
     */
    @PutMapping("/{id}/deactivate")
    @RequirePermission(Permission.ROAD_UPDATE)
    public ResponseEntity<ApiResponse<Void>> deactivateRoad(@PathVariable Long id) {
        roadService.deactivateRoad(id);
        return ResponseEntity.ok(ApiResponse.success("Road deactivated successfully", null));
    }
}

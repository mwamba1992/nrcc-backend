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
import tz.go.roadsfund.nrcc.dto.request.CreateDistrictRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateDistrictRequest;
import tz.go.roadsfund.nrcc.dto.response.ApiResponse;
import tz.go.roadsfund.nrcc.dto.response.DistrictResponse;
import tz.go.roadsfund.nrcc.enums.Permission;
import tz.go.roadsfund.nrcc.security.RequirePermission;
import tz.go.roadsfund.nrcc.service.DistrictService;

import java.util.List;

/**
 * REST Controller for district operations
 */
@RestController
@RequestMapping("/districts")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    /**
     * Create a new district
     */
    @PostMapping
    @RequirePermission(Permission.DISTRICT_CREATE)
    public ResponseEntity<ApiResponse<DistrictResponse>> createDistrict(
            @Valid @RequestBody CreateDistrictRequest request) {
        DistrictResponse district = districtService.createDistrict(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("District created successfully", district));
    }

    /**
     * Update an existing district
     */
    @PutMapping("/{id}")
    @RequirePermission(Permission.DISTRICT_UPDATE)
    public ResponseEntity<ApiResponse<DistrictResponse>> updateDistrict(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDistrictRequest request) {
        DistrictResponse district = districtService.updateDistrict(id, request);
        return ResponseEntity.ok(ApiResponse.success("District updated successfully", district));
    }

    /**
     * Delete a district (soft delete)
     */
    @DeleteMapping("/{id}")
    @RequirePermission(Permission.DISTRICT_DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteDistrict(@PathVariable Long id) {
        districtService.deleteDistrict(id);
        return ResponseEntity.ok(ApiResponse.success("District deleted successfully", null));
    }

    /**
     * Get all districts (paginated)
     */
    @GetMapping
    @RequirePermission(Permission.DISTRICT_READ)
    public ResponseEntity<ApiResponse<Page<DistrictResponse>>> getAllDistricts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<DistrictResponse> districts = districtService.getAllDistrictsPaginated(pageable);
        return ResponseEntity.ok(ApiResponse.success("Districts retrieved successfully", districts));
    }

    /**
     * Get all districts (list)
     */
    @GetMapping("/list")
    @RequirePermission(Permission.DISTRICT_READ)
    public ResponseEntity<ApiResponse<List<DistrictResponse>>> getAllDistrictsList(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        List<DistrictResponse> districts = activeOnly
                ? districtService.getActiveDistricts()
                : districtService.getAllDistricts();
        return ResponseEntity.ok(ApiResponse.success("Districts retrieved successfully", districts));
    }

    /**
     * Get districts by region ID
     */
    @GetMapping("/region/{regionId}")
    @RequirePermission(Permission.DISTRICT_READ)
    public ResponseEntity<ApiResponse<List<DistrictResponse>>> getDistrictsByRegion(
            @PathVariable Long regionId) {
        List<DistrictResponse> districts = districtService.getDistrictsByRegion(regionId);
        return ResponseEntity.ok(ApiResponse.success("Districts retrieved successfully", districts));
    }

    /**
     * Get district by ID
     */
    @GetMapping("/{id}")
    @RequirePermission(Permission.DISTRICT_READ)
    public ResponseEntity<ApiResponse<DistrictResponse>> getDistrictById(@PathVariable Long id) {
        DistrictResponse district = districtService.getDistrictById(id);
        return ResponseEntity.ok(ApiResponse.success("District retrieved successfully", district));
    }

    /**
     * Get district by code
     */
    @GetMapping("/code/{code}")
    @RequirePermission(Permission.DISTRICT_READ)
    public ResponseEntity<ApiResponse<DistrictResponse>> getDistrictByCode(@PathVariable String code) {
        DistrictResponse district = districtService.getDistrictByCode(code);
        return ResponseEntity.ok(ApiResponse.success("District retrieved successfully", district));
    }

    /**
     * Search districts by name
     */
    @GetMapping("/search")
    @RequirePermission(Permission.DISTRICT_READ)
    public ResponseEntity<ApiResponse<Page<DistrictResponse>>> searchDistricts(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DistrictResponse> districts = districtService.searchByName(name, pageable);
        return ResponseEntity.ok(ApiResponse.success("Districts retrieved successfully", districts));
    }

    /**
     * Check if district code exists
     */
    @GetMapping("/exists/code/{code}")
    @RequirePermission(Permission.DISTRICT_READ)
    public ResponseEntity<ApiResponse<Boolean>> existsByCode(@PathVariable String code) {
        boolean exists = districtService.existsByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Check completed", exists));
    }

    /**
     * Get district count
     */
    @GetMapping("/count")
    @RequirePermission(Permission.DISTRICT_READ)
    public ResponseEntity<ApiResponse<Long>> getDistrictCount() {
        long count = districtService.getDistrictCount();
        return ResponseEntity.ok(ApiResponse.success("District count retrieved", count));
    }
}

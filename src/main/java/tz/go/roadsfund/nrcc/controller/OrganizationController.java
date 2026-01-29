package tz.go.roadsfund.nrcc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.go.roadsfund.nrcc.dto.request.CreateOrganizationRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateOrganizationRequest;
import tz.go.roadsfund.nrcc.dto.response.ApiResponse;
import tz.go.roadsfund.nrcc.dto.response.OrganizationResponse;
import tz.go.roadsfund.nrcc.service.OrganizationService;

import java.util.List;

/**
 * REST controller for organization management
 */
@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationResponse>> createOrganization(
            @Valid @RequestBody CreateOrganizationRequest request) {
        OrganizationResponse response = organizationService.createOrganization(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Organization created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganizationById(
            @PathVariable Long id) {
        OrganizationResponse response = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(ApiResponse.success("Organization retrieved successfully", response));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganizationByCode(
            @PathVariable String code) {
        OrganizationResponse response = organizationService.getOrganizationByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Organization retrieved successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> getAllOrganizations() {
        List<OrganizationResponse> response = organizationService.getAllOrganizations();
        return ResponseEntity.ok(ApiResponse.success("Organizations retrieved successfully", response));
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<OrganizationResponse>>> getOrganizationsPaginated(
            Pageable pageable) {
        Page<OrganizationResponse> response = organizationService.getOrganizationsPaginated(pageable);
        return ResponseEntity.ok(ApiResponse.success("Organizations retrieved successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrganizationRequest request) {
        OrganizationResponse response = organizationService.updateOrganization(id, request);
        return ResponseEntity.ok(ApiResponse.success("Organization updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok(ApiResponse.success("Organization deleted successfully", null));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateOrganization(@PathVariable Long id) {
        organizationService.activateOrganization(id);
        return ResponseEntity.ok(ApiResponse.success("Organization activated successfully", null));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateOrganization(@PathVariable Long id) {
        organizationService.deactivateOrganization(id);
        return ResponseEntity.ok(ApiResponse.success("Organization deactivated successfully", null));
    }
}

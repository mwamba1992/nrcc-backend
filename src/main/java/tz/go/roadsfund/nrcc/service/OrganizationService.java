package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.dto.request.CreateOrganizationRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateOrganizationRequest;
import tz.go.roadsfund.nrcc.dto.response.OrganizationResponse;
import tz.go.roadsfund.nrcc.entity.District;
import tz.go.roadsfund.nrcc.entity.Organization;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.exception.ResourceNotFoundException;
import tz.go.roadsfund.nrcc.repository.DistrictRepository;
import tz.go.roadsfund.nrcc.repository.OrganizationRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for organization management operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final DistrictRepository districtRepository;

    public OrganizationResponse createOrganization(CreateOrganizationRequest request) {
        // Check if code already exists
        if (organizationRepository.existsByCode(request.getCode())) {
            throw new BadRequestException("Organization code already exists");
        }

        // Check if name already exists
        if (organizationRepository.existsByName(request.getName())) {
            throw new BadRequestException("Organization name already exists");
        }

        // Fetch district if provided
        District district = null;
        if (request.getDistrictId() != null) {
            district = districtRepository.findById(request.getDistrictId())
                    .orElseThrow(() -> new BadRequestException("District not found"));
        }

        Organization organization = Organization.builder()
                .code(request.getCode())
                .name(request.getName())
                .organizationType(request.getOrganizationType())
                .description(request.getDescription())
                .contactPerson(request.getContactPerson())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .district(district)
                .status("ACTIVE")
                .build();

        organization = organizationRepository.save(organization);
        log.info("Organization created successfully: {}", organization.getName());

        return mapToResponse(organization);
    }

    @Transactional(readOnly = true)
    public OrganizationResponse getOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "id", id));
        return mapToResponse(organization);
    }

    @Transactional(readOnly = true)
    public OrganizationResponse getOrganizationByCode(String code) {
        Organization organization = organizationRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "code", code));
        return mapToResponse(organization);
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<OrganizationResponse> getOrganizationsPaginated(Pageable pageable) {
        return organizationRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public OrganizationResponse updateOrganization(Long id, UpdateOrganizationRequest request) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "id", id));

        // Check if code is being changed and if it's already in use
        if (request.getCode() != null && !request.getCode().equals(organization.getCode())) {
            if (organizationRepository.existsByCode(request.getCode())) {
                throw new BadRequestException("Organization code already in use");
            }
            organization.setCode(request.getCode());
        }

        // Check if name is being changed and if it's already in use
        if (request.getName() != null && !request.getName().equals(organization.getName())) {
            if (organizationRepository.existsByName(request.getName())) {
                throw new BadRequestException("Organization name already in use");
            }
            organization.setName(request.getName());
        }

        // Update other fields
        if (request.getOrganizationType() != null) {
            organization.setOrganizationType(request.getOrganizationType());
        }
        if (request.getDescription() != null) {
            organization.setDescription(request.getDescription());
        }
        if (request.getContactPerson() != null) {
            organization.setContactPerson(request.getContactPerson());
        }
        if (request.getEmail() != null) {
            organization.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            organization.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            organization.setAddress(request.getAddress());
        }
        if (request.getDistrictId() != null) {
            District district = districtRepository.findById(request.getDistrictId())
                    .orElseThrow(() -> new BadRequestException("District not found"));
            organization.setDistrict(district);
        }
        if (request.getStatus() != null) {
            organization.setStatus(request.getStatus());
        }

        organization = organizationRepository.save(organization);
        log.info("Organization updated successfully: {}", organization.getName());

        return mapToResponse(organization);
    }

    public void deleteOrganization(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "id", id));

        organizationRepository.delete(organization);
        log.info("Organization deleted successfully: {}", organization.getName());
    }

    public void activateOrganization(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "id", id));

        organization.setStatus("ACTIVE");
        organizationRepository.save(organization);
        log.info("Organization activated: {}", organization.getName());
    }

    public void deactivateOrganization(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "id", id));

        organization.setStatus("INACTIVE");
        organizationRepository.save(organization);
        log.info("Organization deactivated: {}", organization.getName());
    }

    // Mapping method
    private OrganizationResponse mapToResponse(Organization organization) {
        String districtName = organization.getDistrict() != null ? organization.getDistrict().getName() : null;
        String regionName = organization.getDistrict() != null && organization.getDistrict().getRegion() != null ?
                organization.getDistrict().getRegion().getName() : null;

        return OrganizationResponse.builder()
                .id(organization.getId())
                .code(organization.getCode())
                .name(organization.getName())
                .organizationType(organization.getOrganizationType())
                .description(organization.getDescription())
                .contactPerson(organization.getContactPerson())
                .email(organization.getEmail())
                .phoneNumber(organization.getPhoneNumber())
                .address(organization.getAddress())
                .region(regionName)
                .district(districtName)
                .status(organization.getStatus())
                .build();
    }
}

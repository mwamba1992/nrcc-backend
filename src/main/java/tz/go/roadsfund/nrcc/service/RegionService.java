package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.dto.request.CreateRegionRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateRegionRequest;
import tz.go.roadsfund.nrcc.dto.response.RegionResponse;
import tz.go.roadsfund.nrcc.entity.Region;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.exception.ResourceNotFoundException;
import tz.go.roadsfund.nrcc.repository.RegionRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for region management operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegionService {

    private final RegionRepository regionRepository;

    // ==================== CREATE ====================

    public RegionResponse createRegion(CreateRegionRequest request) {
        // Check if code already exists
        if (regionRepository.existsByCode(request.getCode())) {
            throw new BadRequestException("Region with code '" + request.getCode() + "' already exists");
        }

        // Check if name already exists
        if (regionRepository.existsByName(request.getName())) {
            throw new BadRequestException("Region with name '" + request.getName() + "' already exists");
        }

        Region region = Region.builder()
                .code(request.getCode().toUpperCase())
                .name(request.getName())
                .description(request.getDescription())
                .status("ACTIVE")
                .build();

        region = regionRepository.save(region);
        log.info("Region created successfully: {} - {}", region.getCode(), region.getName());

        return mapToResponse(region);
    }

    // ==================== READ ====================

    @Transactional(readOnly = true)
    public RegionResponse getRegionById(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region", "id", id));
        return mapToResponse(region);
    }

    @Transactional(readOnly = true)
    public RegionResponse getRegionByCode(String code) {
        Region region = regionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Region", "code", code));
        return mapToResponse(region);
    }

    @Transactional(readOnly = true)
    public RegionResponse getRegionByName(String name) {
        Region region = regionRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Region", "name", name));
        return mapToResponse(region);
    }

    @Transactional(readOnly = true)
    public List<RegionResponse> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RegionResponse> getActiveRegions() {
        return regionRepository.findByStatus("ACTIVE").stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<RegionResponse> getRegionsPaginated(Pageable pageable) {
        return regionRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public List<RegionResponse> searchRegionsByName(String name) {
        return regionRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<RegionResponse> searchRegionsByName(String name, Pageable pageable) {
        return regionRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(this::mapToResponse);
    }

    // ==================== UPDATE ====================

    public RegionResponse updateRegion(Long id, UpdateRegionRequest request) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region", "id", id));

        // Check if code is being changed and if it already exists
        if (request.getCode() != null && !request.getCode().equalsIgnoreCase(region.getCode())) {
            if (regionRepository.existsByCode(request.getCode())) {
                throw new BadRequestException("Region with code '" + request.getCode() + "' already exists");
            }
            region.setCode(request.getCode().toUpperCase());
        }

        // Check if name is being changed and if it already exists
        if (request.getName() != null && !request.getName().equals(region.getName())) {
            if (regionRepository.existsByName(request.getName())) {
                throw new BadRequestException("Region with name '" + request.getName() + "' already exists");
            }
            region.setName(request.getName());
        }

        // Update other fields
        if (request.getDescription() != null) {
            region.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            region.setStatus(request.getStatus());
        }

        region = regionRepository.save(region);
        log.info("Region updated successfully: {} - {}", region.getCode(), region.getName());

        return mapToResponse(region);
    }

    // ==================== DELETE ====================

    public void deleteRegion(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region", "id", id));

        // TODO: Check if region has districts before deleting
        // if (districtRepository.existsByRegion(region)) {
        //     throw new BadRequestException("Cannot delete region with existing districts");
        // }

        regionRepository.delete(region);
        log.info("Region deleted successfully: {} - {}", region.getCode(), region.getName());
    }

    // ==================== STATUS MANAGEMENT ====================

    public void activateRegion(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region", "id", id));

        region.setStatus("ACTIVE");
        regionRepository.save(region);
        log.info("Region activated: {} - {}", region.getCode(), region.getName());
    }

    public void deactivateRegion(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region", "id", id));

        region.setStatus("INACTIVE");
        regionRepository.save(region);
        log.info("Region deactivated: {} - {}", region.getCode(), region.getName());
    }

    // ==================== STATISTICS ====================

    @Transactional(readOnly = true)
    public long countAllRegions() {
        return regionRepository.count();
    }

    @Transactional(readOnly = true)
    public long countActiveRegions() {
        return regionRepository.countByStatus("ACTIVE");
    }

    // ==================== MAPPING ====================

    private RegionResponse mapToResponse(Region region) {
        return RegionResponse.builder()
                .id(region.getId())
                .code(region.getCode())
                .name(region.getName())
                .description(region.getDescription())
                .status(region.getStatus())
                .build();
    }
}

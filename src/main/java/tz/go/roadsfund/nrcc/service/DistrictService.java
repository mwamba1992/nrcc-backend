package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.dto.request.CreateDistrictRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateDistrictRequest;
import tz.go.roadsfund.nrcc.dto.response.DistrictResponse;
import tz.go.roadsfund.nrcc.entity.District;
import tz.go.roadsfund.nrcc.entity.Region;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.exception.ResourceNotFoundException;
import tz.go.roadsfund.nrcc.repository.DistrictRepository;
import tz.go.roadsfund.nrcc.repository.RegionRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for district operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DistrictService {

    private final DistrictRepository districtRepository;
    private final RegionRepository regionRepository;

    @Transactional
    public DistrictResponse createDistrict(CreateDistrictRequest request) {
        if (districtRepository.existsByCode(request.getCode())) {
            throw new BadRequestException("District with code '" + request.getCode() + "' already exists");
        }

        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region", "id", request.getRegionId()));

        District district = District.builder()
                .code(request.getCode())
                .name(request.getName())
                .region(region)
                .description(request.getDescription())
                .status("ACTIVE")
                .build();

        district = districtRepository.save(district);
        log.info("Created district: {}", district.getCode());
        return mapToResponse(district);
    }

    @Transactional
    public DistrictResponse updateDistrict(Long id, UpdateDistrictRequest request) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District", "id", id));

        if (request.getCode() != null && !request.getCode().equals(district.getCode())) {
            if (districtRepository.existsByCode(request.getCode())) {
                throw new BadRequestException("District with code '" + request.getCode() + "' already exists");
            }
            district.setCode(request.getCode());
        }

        if (request.getName() != null) {
            district.setName(request.getName());
        }

        if (request.getRegionId() != null && !request.getRegionId().equals(district.getRegion().getId())) {
            Region region = regionRepository.findById(request.getRegionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Region", "id", request.getRegionId()));
            district.setRegion(region);
        }

        if (request.getDescription() != null) {
            district.setDescription(request.getDescription());
        }

        district = districtRepository.save(district);
        log.info("Updated district: {}", district.getCode());
        return mapToResponse(district);
    }

    @Transactional
    public void deleteDistrict(Long id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District", "id", id));

        district.setStatus("DELETED");
        districtRepository.save(district);
        log.info("Soft deleted district: {}", district.getCode());
    }

    public Page<DistrictResponse> getAllDistrictsPaginated(Pageable pageable) {
        return districtRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public List<DistrictResponse> getAllDistricts() {
        return districtRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DistrictResponse> getActiveDistricts() {
        return districtRepository.findByStatus("ACTIVE").stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DistrictResponse> getDistrictsByRegion(Long regionId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new ResourceNotFoundException("Region", "id", regionId));
        return districtRepository.findByRegion(region).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DistrictResponse getDistrictById(Long id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District", "id", id));
        return mapToResponse(district);
    }

    public DistrictResponse getDistrictByCode(String code) {
        District district = districtRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("District", "code", code));
        return mapToResponse(district);
    }

    public Page<DistrictResponse> searchByName(String name, Pageable pageable) {
        return districtRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(this::mapToResponse);
    }

    public long getDistrictCount() {
        return districtRepository.count();
    }

    public boolean existsByCode(String code) {
        return districtRepository.existsByCode(code);
    }

    private DistrictResponse mapToResponse(District district) {
        return DistrictResponse.builder()
                .id(district.getId())
                .code(district.getCode())
                .name(district.getName())
                .regionId(district.getRegion().getId())
                .regionName(district.getRegion().getName())
                .description(district.getDescription())
                .status(district.getStatus())
                .build();
    }
}

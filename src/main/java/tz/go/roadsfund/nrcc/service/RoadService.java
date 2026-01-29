package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.dto.request.CreateRoadRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateRoadRequest;
import tz.go.roadsfund.nrcc.dto.response.RoadResponse;
import tz.go.roadsfund.nrcc.entity.Road;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.exception.ResourceNotFoundException;
import tz.go.roadsfund.nrcc.repository.RoadRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for road management operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoadService {

    private final RoadRepository roadRepository;

    public RoadResponse createRoad(CreateRoadRequest request) {
        // Check if road with same number already exists
        if (request.getRoadNumber() != null && roadRepository.existsByRoadNumber(request.getRoadNumber())) {
            throw new BadRequestException("Road with number '" + request.getRoadNumber() + "' already exists");
        }

        Road road = Road.builder()
                .name(request.getName())
                .roadNumber(request.getRoadNumber())
                .length(request.getLength())
                .currentClass(request.getCurrentClass())
                .startPoint(request.getStartPoint())
                .endPoint(request.getEndPoint())
                .region(request.getRegion())
                .district(request.getDistrict())
                .surfaceType(request.getSurfaceType())
                .carriagewayWidth(request.getCarriagewayWidth())
                .formationWidth(request.getFormationWidth())
                .roadReserveWidth(request.getRoadReserveWidth())
                .description(request.getDescription())
                .status("ACTIVE")
                .build();

        road = roadRepository.save(road);
        log.info("Road created successfully: {} - {}", road.getRoadNumber(), road.getName());

        return mapToResponse(road);
    }

    @Transactional(readOnly = true)
    public RoadResponse getRoadById(Long id) {
        Road road = roadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road", "id", id));
        return mapToResponse(road);
    }

    @Transactional(readOnly = true)
    public RoadResponse getRoadByNumber(String roadNumber) {
        Road road = roadRepository.findByRoadNumber(roadNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Road", "roadNumber", roadNumber));
        return mapToResponse(road);
    }

    @Transactional(readOnly = true)
    public List<RoadResponse> getAllRoads() {
        return roadRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<RoadResponse> getRoadsPaginated(Pageable pageable) {
        return roadRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public List<RoadResponse> getRoadsByRegion(String region) {
        return roadRepository.findByRegion(region).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoadResponse> getRoadsByDistrict(String district) {
        return roadRepository.findByDistrict(district).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoadResponse> getRoadsByStatus(String status) {
        return roadRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RoadResponse updateRoad(Long id, UpdateRoadRequest request) {
        Road road = roadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road", "id", id));

        // Check if road number is being changed and if it already exists
        if (request.getRoadNumber() != null && !request.getRoadNumber().equals(road.getRoadNumber())) {
            if (roadRepository.existsByRoadNumber(request.getRoadNumber())) {
                throw new BadRequestException("Road with number '" + request.getRoadNumber() + "' already exists");
            }
            road.setRoadNumber(request.getRoadNumber());
        }

        // Update fields if provided
        if (request.getName() != null) {
            road.setName(request.getName());
        }
        if (request.getLength() != null) {
            road.setLength(request.getLength());
        }
        if (request.getCurrentClass() != null) {
            road.setCurrentClass(request.getCurrentClass());
        }
        if (request.getStartPoint() != null) {
            road.setStartPoint(request.getStartPoint());
        }
        if (request.getEndPoint() != null) {
            road.setEndPoint(request.getEndPoint());
        }
        if (request.getRegion() != null) {
            road.setRegion(request.getRegion());
        }
        if (request.getDistrict() != null) {
            road.setDistrict(request.getDistrict());
        }
        if (request.getSurfaceType() != null) {
            road.setSurfaceType(request.getSurfaceType());
        }
        if (request.getCarriagewayWidth() != null) {
            road.setCarriagewayWidth(request.getCarriagewayWidth());
        }
        if (request.getFormationWidth() != null) {
            road.setFormationWidth(request.getFormationWidth());
        }
        if (request.getRoadReserveWidth() != null) {
            road.setRoadReserveWidth(request.getRoadReserveWidth());
        }
        if (request.getDescription() != null) {
            road.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            road.setStatus(request.getStatus());
        }

        road = roadRepository.save(road);
        log.info("Road updated successfully: {} - {}", road.getRoadNumber(), road.getName());

        return mapToResponse(road);
    }

    public void deleteRoad(Long id) {
        Road road = roadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road", "id", id));

        roadRepository.delete(road);
        log.info("Road deleted successfully: {} - {}", road.getRoadNumber(), road.getName());
    }

    public void activateRoad(Long id) {
        Road road = roadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road", "id", id));

        road.setStatus("ACTIVE");
        roadRepository.save(road);
        log.info("Road activated: {} - {}", road.getRoadNumber(), road.getName());
    }

    public void deactivateRoad(Long id) {
        Road road = roadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road", "id", id));

        road.setStatus("INACTIVE");
        roadRepository.save(road);
        log.info("Road deactivated: {} - {}", road.getRoadNumber(), road.getName());
    }

    // Mapping method
    private RoadResponse mapToResponse(Road road) {
        return RoadResponse.builder()
                .id(road.getId())
                .name(road.getName())
                .roadNumber(road.getRoadNumber())
                .length(road.getLength())
                .currentClass(road.getCurrentClass().name())
                .startPoint(road.getStartPoint())
                .endPoint(road.getEndPoint())
                .region(road.getRegion())
                .district(road.getDistrict())
                .surfaceType(road.getSurfaceType())
                .carriagewayWidth(road.getCarriagewayWidth())
                .formationWidth(road.getFormationWidth())
                .roadReserveWidth(road.getRoadReserveWidth())
                .description(road.getDescription())
                .status(road.getStatus())
                .createdAt(road.getCreatedAt())
                .updatedAt(road.getUpdatedAt())
                .build();
    }
}

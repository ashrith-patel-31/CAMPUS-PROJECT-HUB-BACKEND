package com.campus.campus_project_hub_backend.service;

import com.campus.campus_project_hub_backend.entity.JoinRequest;
import com.campus.campus_project_hub_backend.repository.JoinRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;

    public JoinRequestService(JoinRequestRepository joinRequestRepository) {
        this.joinRequestRepository = joinRequestRepository;
    }

    // CREATE
    public JoinRequest createRequest(JoinRequest request) {

        boolean alreadyExists =
                joinRequestRepository.existsByProjectIdAndStudentName(
                        request.getProjectId(),
                        request.getStudentName()
                );

        if (alreadyExists) {
            return null;
        }

        request.setStatus("Pending");

        return joinRequestRepository.save(request);
    }

    // READ ALL
    public List<JoinRequest> getAllRequests() {
        return joinRequestRepository.findAll();
    }

    // UPDATE STATUS
    public JoinRequest updateRequestStatus(Long id, String status) {

        Optional<JoinRequest> existingRequest =
                joinRequestRepository.findById(id);

        if (existingRequest.isPresent()) {

            JoinRequest request = existingRequest.get();

            request.setStatus(status);

            return joinRequestRepository.save(request);
        }

        return null;
    }
}
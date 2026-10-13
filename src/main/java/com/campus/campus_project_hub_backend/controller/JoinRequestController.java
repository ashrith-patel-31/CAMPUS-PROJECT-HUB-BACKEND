package com.campus.campus_project_hub_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_project_hub_backend.entity.JoinRequest;
import com.campus.campus_project_hub_backend.service.JoinRequestService;

@RestController
@RequestMapping("/api/join-requests")
@CrossOrigin(origins = "http://localhost:5173")
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    public JoinRequestController(JoinRequestService joinRequestService) {
        this.joinRequestService = joinRequestService;
    }

    // CREATE JOIN REQUEST
    @PostMapping
    public ResponseEntity<JoinRequest> createRequest(
            @RequestBody JoinRequest request) {

        JoinRequest createdRequest =
                joinRequestService.createRequest(request);

        if (createdRequest == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(createdRequest);
    }

    // GET ALL JOIN REQUESTS
    @GetMapping
    public ResponseEntity<List<JoinRequest>> getAllRequests() {

        List<JoinRequest> requests =
                joinRequestService.getAllRequests();

        return ResponseEntity.ok(requests);
    }

    // UPDATE REQUEST STATUS
    @PutMapping("/{id}/status")
    public ResponseEntity<JoinRequest> updateRequestStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        JoinRequest updatedRequest =
                joinRequestService.updateRequestStatus(id, status);

        if (updatedRequest == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedRequest);
    }
}
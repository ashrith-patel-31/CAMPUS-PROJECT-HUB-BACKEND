package com.campus.campus_project_hub_backend.repository;

import com.campus.campus_project_hub_backend.entity.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinRequestRepository
        extends JpaRepository<JoinRequest, Long> {

    boolean existsByProjectIdAndStudentName(
            Long projectId,
            String studentName
    );
}
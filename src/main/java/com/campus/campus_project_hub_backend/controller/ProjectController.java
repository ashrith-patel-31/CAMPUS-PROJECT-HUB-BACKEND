package com.campus.campus_project_hub_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_project_hub_backend.entity.Project;
import com.campus.campus_project_hub_backend.repository.ProjectRepository;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://campus-project-hub-frontend.onrender.com"
})
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Get all projects
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectRepository.findAll());
    }

    // Get one project
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(
            @PathVariable Long id) {

        return projectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a project
    @PostMapping
    public ResponseEntity<?> createProject(
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestBody Project project) {

        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Always assign ownership from the request identity,
        // not from ownerId supplied in the project body.
        project.setId(null);
        project.setOwnerId(currentUserId);

        if (project.getOwnerName() == null ||
                project.getOwnerName().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Owner name is required.");
        }

        Project savedProject = projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    // Update a project only if the requester owns it
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestBody Project updatedProject) {

        Project existingProject = projectRepository.findById(id)
                .orElse(null);

        if (existingProject == null) {
            return ResponseEntity.notFound().build();
        }

        if (currentUserId == null ||
                existingProject.getOwnerId() == null ||
                !existingProject.getOwnerId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only edit your own projects.");
        }

        existingProject.setTitle(updatedProject.getTitle());
        existingProject.setDescription(updatedProject.getDescription());
        existingProject.setCategory(updatedProject.getCategory());
        existingProject.setSkills(updatedProject.getSkills());
        existingProject.setTeamSize(updatedProject.getTeamSize());

        // Do not allow the request body to change project ownership.
        Project savedProject = projectRepository.save(existingProject);

        return ResponseEntity.ok(savedProject);
    }

    // Delete a project only if the requester owns it
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long currentUserId) {

        Project existingProject = projectRepository.findById(id)
                .orElse(null);

        if (existingProject == null) {
            return ResponseEntity.notFound().build();
        }

        if (currentUserId == null ||
                existingProject.getOwnerId() == null ||
                !existingProject.getOwnerId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only delete your own projects.");
        }

        projectRepository.delete(existingProject);

        return ResponseEntity.noContent().build();
    }
}
package com.campus.campus_project_hub_backend.service;

import com.campus.campus_project_hub_backend.entity.Project;
import com.campus.campus_project_hub_backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project updateProject(Long id, Project updatedProject) {

        Optional<Project> existingProject =
                projectRepository.findById(id);

        if (existingProject.isPresent()) {

            Project project = existingProject.get();

            project.setTitle(updatedProject.getTitle());
            project.setDescription(updatedProject.getDescription());
            project.setCategory(updatedProject.getCategory());
            project.setTeamSize(updatedProject.getTeamSize());
            project.setSkills(updatedProject.getSkills());

            // Preserve the original project owner
            // if the update request does not contain owner details.
            if (updatedProject.getOwnerId() != null) {
                project.setOwnerId(updatedProject.getOwnerId());
            }

            if (updatedProject.getOwnerName() != null) {
                project.setOwnerName(updatedProject.getOwnerName());
            }

            return projectRepository.save(project);
        }

        return null;
    }

    public boolean deleteProject(Long id) {

        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
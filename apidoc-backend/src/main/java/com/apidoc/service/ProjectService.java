package com.apidoc.service;

import com.apidoc.entity.Project;

import java.util.List;

public interface ProjectService {
    List<Project> list();

    Project getById(Long id);

    Project create(Project project);

    Project update(Project project);

    void delete(Long id);
}

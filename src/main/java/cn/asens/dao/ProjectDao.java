package cn.asens.dao;

import cn.asens.entity.Project;
import cn.asens.entity.ProjectFile;

import java.util.List;

public interface ProjectDao {
    void save(Project project);

    void update(Project project);

    void delete(Project project);

    Project findById(Integer id);

    List<Project> getList();
}

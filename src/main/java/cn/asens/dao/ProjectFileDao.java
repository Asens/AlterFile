package cn.asens.dao;

import cn.asens.entity.ProjectFile;

import java.util.List;

public interface ProjectFileDao {

    void save(ProjectFile projectFile);

    void update(ProjectFile projectFile);

    void delete(ProjectFile projectFile);

    ProjectFile findById(Integer id);

    ProjectFile findByAbsolutePath(String absolutePath);

    List<ProjectFile> getListByProjectId(Integer id);

    void updateAllToDefault(Integer projectId);

    List<ProjectFile> getNewList();

    List<ProjectFile> getModifyList(Integer projectId);
}

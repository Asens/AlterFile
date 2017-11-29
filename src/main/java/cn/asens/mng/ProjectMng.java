package cn.asens.mng;

import cn.asens.entity.Project;
import cn.asens.entity.ProjectFile;

import java.io.IOException;
import java.util.List;


/**
 * @author Asens
 * create 2017-09-09 13:53
 **/

public interface ProjectMng {
    void save(Project project);

    void update(Project project);

    void delete(Project project);

    Project findById(Integer id);

    List<Project> getList();

    void init(Project project) throws IOException;

    List<ProjectFile> changeList(Project project) throws IOException;


    List<ProjectFile> newList(Project project) throws IOException;

    void updateAllToDefault(Integer projectId) throws IOException;
}

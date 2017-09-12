package cn.asens.dao.impl;

import cn.asens.dao.ProjectFileDao;
import cn.asens.entity.ProjectFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Asens
 * create 2017-09-09 12:05
 **/
@Repository
public class ProjectFileDaoImpl extends BaseDaoImpl implements ProjectFileDao{
    @Override
    public void save(ProjectFile projectFile) {
        getSession().save(projectFile);
    }

    @Override
    public void update(ProjectFile projectFile) {
        getSession().update(projectFile);
    }

    @Override
    public void delete(ProjectFile projectFile) {
        getSession().delete(projectFile);
    }

    @Override
    public ProjectFile findById(Integer id) {
        return getSession().get(ProjectFile.class,id);
    }

    @Override
    public ProjectFile findByAbsolutePath(String absolutePath) {
        return (ProjectFile)getSession().createQuery("from ProjectFile bean where bean.absolutePath=:absolutePath")
                .setParameter("absolutePath",absolutePath)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public List<ProjectFile> getListByProjectId(Integer id) {
        return getSession().createQuery("from ProjectFile bean where bean.projectId=:id")
                .setParameter("id",id)
                .list();
    }

    @Override
    public void updateAllToDefault(Integer projectId) {
        getSession().createSQLQuery("update project_file set status=0").executeUpdate();
    }

    @Override
    public List<ProjectFile> getNewList() {
        return getSession().createQuery("from ProjectFile bean where bean.status=:status")
                .setParameter("status",ProjectFile.STATUS_ADD)
                .list();
    }

    @Override
    public List<ProjectFile> getModifyList(Integer projectId) {
        return getSession().createQuery("from ProjectFile bean where bean.status=:status")
                .setParameter("status",ProjectFile.STATUS_MODIFY)
                .list();
    }
}

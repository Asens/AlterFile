package cn.asens.dao.impl;

import cn.asens.dao.ProjectDao;
import cn.asens.entity.Project;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Asens
 * create 2017-09-09 12:36
 **/
@Repository
public class ProjectDaoImpl extends BaseDaoImpl implements ProjectDao{
    @Override
    public void save(Project project) {
        getSession().save(project);
    }

    @Override
    public void update(Project project) {
        getSession().update(project);
    }

    @Override
    public void delete(Project project) {
        getSession().delete(project);
    }

    @Override
    public Project findById(Integer id) {
        return getSession().get(Project.class,id);
    }

    @Override
    public List<Project> getList() {
        return getSession().createQuery("from Project").list();
    }
}

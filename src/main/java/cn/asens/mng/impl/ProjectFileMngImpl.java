package cn.asens.mng.impl;

import cn.asens.dao.ProjectDao;
import cn.asens.dao.ProjectFileDao;
import cn.asens.entity.ProjectFile;
import cn.asens.mng.ProjectFileMng;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Asens
 * create 2017-09-09 20:45
 **/
@Service
@Transactional
public class ProjectFileMngImpl implements ProjectFileMng{
    @Resource
    private ProjectFileDao projectFileDao;

    @Resource
    private ProjectDao projectDao;


    @Override
    public ProjectFile findById(Integer fileId) {
        return projectFileDao.findById(fileId);
    }


    @Override
    public void update(ProjectFile pf) {
        projectFileDao.update(pf);
    }
}

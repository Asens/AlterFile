package cn.asens.mng.impl;

import cn.asens.dao.ProjectDao;
import cn.asens.dao.ProjectFileDao;
import cn.asens.entity.Project;
import cn.asens.entity.ProjectFile;
import cn.asens.mng.ProjectMng;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author Asens
 * create 2017-09-09 13:54
 **/
@Transactional
@Service
public class ProjectMngImpl implements ProjectMng {
    @Resource
    private ProjectDao projectDao;

    @Resource
    private ProjectFileDao projectFileDao;

    @Override
    public void save(Project project) {
        projectDao.save(project);
    }

    @Override
    public void update(Project project) {
        projectDao.update(project);
    }

    @Override
    public void delete(Project project) {
        projectDao.delete(project);
    }

    @Override
    public Project findById(Integer id) {
        return projectDao.findById(id);
    }

    @Override
    public List<Project> getList() {
        return projectDao.getList();
    }

    @Override
    public void init(Project project) throws IOException {
        String filePath=project.getBasePath();

        File file=new File(filePath);

        if(!file.exists() || !file.canRead()){
            throw new IOException();
        }

        List<File> list=new ArrayList<>();
        scanFile(file,list);
        list.forEach(projectFile->saveProjectFile(projectFile,project,ProjectFile.STATUS_DEFAULT)) ;
        project.setInitialized(Project.HAS_INITIALIZED);
        projectDao.update(project);
    }



    @Override
    public List<ProjectFile> changeList(Project project) throws IOException {
        List<File> curList=new ArrayList<>();
        scanFile(new File(project.getBasePath()),curList);
        List<ProjectFile> list=projectFileDao.getListByProjectId(project.getId());
        return list.stream().filter(projectFile->isChange(projectFile,curList)).collect(toList());
    }

    @Override
    public List<ProjectFile> newList(Project project) throws IOException {
        List<File> curList=new ArrayList<>();
        scanFile(new File(project.getBasePath()),curList);
        List<ProjectFile> list=projectFileDao.getListByProjectId(project.getId());
        List<ProjectFile> newList=projectFileDao.getNewList();
        List<ProjectFile> resultList;
        if(newList!=null&&newList.size()>0){
            resultList=new ArrayList<>(newList);
        }else{
            resultList=new ArrayList<>();
        }

        if(list==null||list.size()==0){
            return resultList;
        }

        for(File file:curList){
            if(!fileInList(file,list)){
                ProjectFile pf=saveProjectFile(file,project,ProjectFile.STATUS_ADD);
                resultList.add(pf);
            }
        }
        return resultList;
    }

    @Override
    public void updateAllToDefault(Integer projectId) throws IOException {
        List<ProjectFile> list=projectFileDao.getModifyList(projectId);
        Project project=projectDao.findById(projectId);
        List<File> curList=new ArrayList<>();
        scanFile(new File(project.getBasePath()),curList);

        list.forEach(projectFile->{
            for(File file:curList){
                if(file.getAbsolutePath().equals(projectFile.getAbsolutePath())){
                    projectFile.setLastModify(file.lastModified());
                    projectFileDao.update(projectFile);
                }
            }
        });
        projectFileDao.updateAllToDefault(projectId);
    }

    private boolean fileInList(File file,List<ProjectFile> list){
        for(ProjectFile pf:list){
            if(pf.getAbsolutePath().equals(file.getAbsolutePath())){
                return true;
            }
        }
        return false;
    }

    private ProjectFile saveProjectFile(File projectFile,Project project,Integer status) {
        ProjectFile pf=new ProjectFile();
        pf.setAbsolutePath(projectFile.getAbsolutePath());
        pf.setFileName(projectFile.getName());
        pf.setLastModify(projectFile.lastModified());
        pf.setStatus(status);
        pf.setProjectId(project.getId());
        projectFileDao.save(pf);
        return pf;
    }

    private void scanFile(File file, List<File> list) throws IOException {
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files==null) throw new IOException();
            for(File child:files){
                scanFile(child,list);
            }
        }else{
            list.add(file);
        }
    }

    private boolean isChange(ProjectFile projectFile, List<File> list) {
        for(File file:list){
            if(file.getAbsolutePath().equals(projectFile.getAbsolutePath())){
                if(file.lastModified() != projectFile.getLastModify()){
                    projectFile.setStatus(ProjectFile.STATUS_MODIFY);
                    projectFileDao.update(projectFile);
                    return true;
                }

            }
        }
        return false;
    }
}

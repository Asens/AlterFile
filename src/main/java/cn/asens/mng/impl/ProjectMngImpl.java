package cn.asens.mng.impl;

import cn.asens.dao.ProjectDao;
import cn.asens.dao.ProjectFileDao;
import cn.asens.entity.Project;
import cn.asens.entity.ProjectFile;
import cn.asens.mng.ProjectMng;
import cn.asens.util.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;

/**
 * @author Asens
 * create 2017-09-09 13:54
 **/
@Transactional
@Service
public class ProjectMngImpl implements ProjectMng {
    private static Map<String,List<File>> fileListMap=new ConcurrentHashMap<>();
    private static Map<String,String> excludeMap=new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        ExecutorService es= Executors.newSingleThreadExecutor();
        es.execute(new FileThread());
    }

    class FileThread extends Thread{

        @Override
        public void run(){
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                fileListMap.forEach((root,fileList)->{
                    List<File> curList=new ArrayList<>();
                    try {
                        scanFile(new File(root),curList,excludeMap.get(root));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileListMap.put(root,curList);
                });
            }
        }
    }

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
        scanFile(file,list,project.getExcludePath());
        list.forEach(projectFile->saveProjectFile(projectFile,project,
                ProjectFile.STATUS_DEFAULT)) ;
        project.setInitialized(Project.HAS_INITIALIZED);
        projectDao.update(project);
    }



    @Override
    public List<ProjectFile> changeList(Project project) throws IOException {
        List<ProjectFile> list=projectFileDao.getListByProjectId(project.getId());
        List<ProjectFile> result=new ArrayList<>();

        List<File> fileList=fileListMap.get(project.getBasePath());
        if(fileList.size()>0){
            Map<String,File> map=new HashMap<>(fileList.size()+1);
            fileList.forEach(f->map.put(f.getAbsolutePath(),f));
            list.forEach(pf->{
                String path=pf.getAbsolutePath();
                long time=pf.getLastModify();
                File file=map.get(path);

                if(file!=null&&file.exists()&&file.lastModified()!=time
                        &&!matchExcludePath(file.getAbsolutePath(),
                        project.getExcludePath())){
                    result.add(pf);
                }
            });
        }else{
            list.forEach(pf->{
                String path=pf.getAbsolutePath();
                long time=pf.getLastModify();

                File file=new File(path);
                if(file.exists()&&file.lastModified()!=time
                        &&!matchExcludePath(file.getAbsolutePath(),project.getExcludePath())){
                    result.add(pf);
                }
            });
        }

        return result;
    }



    @Override
    public List<ProjectFile> newList(Project project) throws IOException {
        List<File> curList;

        if(!fileListMap.containsKey(project.getBasePath())||
                (curList=fileListMap.get(project.getBasePath())).size()==0){
            excludeMap.put(project.getBasePath(),project.getExcludePath());
            fileListMap.put(project.getBasePath(),new ArrayList<>());
            curList=new ArrayList<>();
            scanFile(new File(project.getBasePath()),curList,project.getExcludePath());
        }


        List<ProjectFile> list=projectFileDao.getListByProjectId(project.getId());
        List<ProjectFile> newList=projectFileDao.getNewList(project.getId());


        List<ProjectFile> resultList;
        if(newList!=null&&newList.size()>0){
            resultList=new ArrayList<>(newList);
        }else{
            resultList=new ArrayList<>();
        }

        if(list==null||list.size()==0){
            return resultList;
        }

        Map<String,ProjectFile> map=new HashMap<>(list.size()+1);
        list.forEach(pf-> map.put(pf.getAbsolutePath(),pf));

        for(File file:curList){
            if(!map.containsKey(file.getAbsolutePath())){
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
        scanFile(new File(project.getBasePath()),curList,project.getExcludePath());

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

    private ProjectFile saveProjectFile(File projectFile,Project project,
                                        Integer status) {
        ProjectFile pf=new ProjectFile();
        pf.setAbsolutePath(projectFile.getAbsolutePath());
        pf.setFileName(projectFile.getName());
        pf.setLastModify(projectFile.lastModified());
        pf.setStatus(status);
        pf.setProjectId(project.getId());
        projectFileDao.save(pf);
        return pf;
    }

    private void scanFile(File file, List<File> list,String excludePath)
            throws IOException {
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files==null) throw new IOException();
            for(File child:files){
                scanFile(child,list,excludePath);
            }
        }else{
            if(matchExcludePath(file.getAbsolutePath(),excludePath)){
                return;
            }
            list.add(file);
        }
    }

    private boolean matchExcludePath(String absolutePath, String excludePath) {
        if(StringUtils.isBlank(excludePath)){
            return false;
        }

        if(!excludePath.contains(";")){
            return doMatchExcludePath(absolutePath,excludePath);
        }

        String[] arr=excludePath.split(";");
        for(String ex:arr){
            if(StringUtils.isBlank(ex)) continue;
            if(doMatchExcludePath(absolutePath,ex)){
                return true;
            }
        }

        return false;
    }

    private boolean doMatchExcludePath(String absolutePath, String ex) {
        if(ex.startsWith("*.")){
            ex=ex.replace("*","");
            return absolutePath.endsWith(ex);
        }
        return false;
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

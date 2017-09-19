package cn.asens.controller;

import cn.asens.entity.Project;
import cn.asens.entity.ProjectFile;
import cn.asens.mng.ProjectFileMng;
import cn.asens.mng.ProjectMng;
import cn.asens.util.HttpUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Asens
 * create 2017-09-13 21:26
 **/
@Controller
public class FileAct {
    private static Logger log= LogManager.getLogger(SampleController.class);

    @Resource
    private ProjectFileMng projectFileMng;
    @Resource
    private ProjectMng projectMng;

    @RequestMapping("/download/{fileId}")
    public void download(@PathVariable Integer fileId, HttpServletResponse response, HttpServletRequest request) throws IOException {
        ProjectFile projectFile=projectFileMng.findById(fileId);
        File file=new File(projectFile.getAbsolutePath());
        response.setContentType(request.getSession().getServletContext().getMimeType(file.getName()));
        response.setHeader("Content-Disposition", "attachment;filename="+file.getName());
        try(BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file))){
            int b;
            while((b=bis.read())!=-1){
                response.getWriter().write(b);
            }
        }catch (Exception ignore){}
    }

    @RequestMapping("/push/{fileId}")
    public void push(@PathVariable Integer fileId, HttpServletResponse response, HttpServletRequest request) throws IOException {
        ProjectFile projectFile=projectFileMng.findById(fileId);
        Project project=projectMng.findById(projectFile.getProjectId());
        File file=new File(projectFile.getAbsolutePath());
        try {
            HttpUtils.upload(file,project.getServerUploadPath(),relativePath(projectFile,project));
        }catch (IOException e){
            response.getWriter().write("fail,IOException");
            return;
        }

        response.getWriter().write("success");

    }

    @RequestMapping("/pushAll/{id}")
    public void pushAll(@PathVariable Integer id, HttpServletResponse response, HttpServletRequest request) throws IOException {
        Project project=projectMng.findById(id);
        List<ProjectFile> newList=projectMng.newList(project);
        List<ProjectFile> changeList=projectMng.changeList(project);
        List<ProjectFile> list=new ArrayList<>(newList);
        list.addAll(changeList);
        List<Integer> successList=new ArrayList<>();
        for(ProjectFile pf:list){
            try {
                HttpUtils.upload(new File(pf.getAbsolutePath()),project.getServerUploadPath(),relativePath(pf,project));
                successList.add(pf.getId());
            }catch (IOException ignore){}
        }
        JSONObject result=new JSONObject();
        JSONArray array=new JSONArray();
        successList.forEach(array::put);
        result.put("files",array);
        result.put("total",list.size());
        result.put("sent",successList.size());

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result.toString());

    }

    private String relativePath(ProjectFile projectFile, Project project) {
        return project.getRemotePath()+projectFile.getAbsolutePath().replace(project.getBasePath(),"");
    }

    @RequestMapping("/upload")
    public void upload(@RequestParam(value = "file", required = false) MultipartFile file,String remotePath, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("remotePath : "+remotePath);
        remotePath=remotePath.replace("\\",File.separator);
        File tmpFile=new File(remotePath);
        if(!tmpFile.exists()) tmpFile.getParentFile().mkdirs();

        try {
            file.transferTo(tmpFile);
            response.getWriter().write("success");
        } catch (IOException e) {
            response.getWriter().write("fail IOException");
        }

    }

}

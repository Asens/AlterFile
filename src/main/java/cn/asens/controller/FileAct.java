package cn.asens.controller;

import cn.asens.entity.Project;
import cn.asens.entity.ProjectFile;
import cn.asens.mng.ProjectFileMng;
import cn.asens.util.HttpUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
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

        File file=new File(projectFile.getAbsolutePath());
        try {
            HttpUtils.upload(file);
        }catch (IOException e){
            response.getWriter().write("fail,IOException");
            return;
        }

        response.getWriter().write("success");

    }

    @RequestMapping("/upload")
    public void upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException, org.apache.commons.fileupload.FileUploadException {
        log.info(file);
        File tmpFile=new File("D:\\test\\"+file.getOriginalFilename());
        file.transferTo(tmpFile);
        response.getWriter().write("success");
    }

}

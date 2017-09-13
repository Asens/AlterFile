package cn.asens.controller;

import cn.asens.entity.ProjectFile;
import cn.asens.mng.ProjectFileMng;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Asens
 * create 2017-09-13 21:26
 **/
@Controller
public class FileAct {
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

}

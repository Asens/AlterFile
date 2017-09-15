package cn.asens.controller;

import cn.asens.controller.componet.TreeNode;
import cn.asens.dao.ProjectDao;
import cn.asens.dao.UserDao;
import cn.asens.entity.Project;
import cn.asens.entity.ProjectFile;
import cn.asens.entity.User;
import cn.asens.mng.ProjectFileMng;
import cn.asens.mng.ProjectMng;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Asens on 2017/7/13
 */

@Controller
public class SampleController {
    private static Logger log= LogManager.getLogger(SampleController.class);

    @Resource
    private ProjectMng projectMng;



    @RequestMapping("/")
    public String home(ModelMap model) {
        List<Project> list=projectMng.getList();
        model.put("list",list);
        return "main";
    }

    @RequestMapping("/project/add")
    public String add(ModelMap model) {
        return "add";
    }

    @RequestMapping("/project/save")
    public String update(Project project) {
        project.setInitialized(Project.NOT_INITIALIZED);
        projectMng.save(project);
        return "redirect:/";
    }

    @RequestMapping("/project/{id}")
    public String project(@PathVariable Integer id, ModelMap model) {
        Project project=projectMng.findById(id);
        model.put("project",project);
        return "project";
    }

    @RequestMapping("/project/{id}/init")
    public void init(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Project project=projectMng.findById(id);
        try {
            projectMng.init(project);
        }catch (IOException e){
            response.getWriter().write("fail");
            return;
        }
        response.getWriter().write("success");
    }

    @RequestMapping("/project/{id}/changeList")
    public String changeList(@PathVariable Integer id, HttpServletResponse response,ModelMap model) throws IOException {
        Project project=projectMng.findById(id);
        try {
            List<ProjectFile> list=projectMng.changeList(project);
            model.put("list",list);
        }catch (Exception e){
            response.getWriter().write("fail");
            return null;
        }
        return "project/changeList";
    }

    @RequestMapping("/project/{id}/newList")
    public String newList(@PathVariable Integer id, HttpServletResponse response,ModelMap model) throws IOException {
        Project project=projectMng.findById(id);
        try {
            List<ProjectFile> list=projectMng.newList(project);
            model.put("list",list);
        }catch (Exception e){
            response.getWriter().write("fail");
            return null;
        }
        return "project/newList";
    }

    @RequestMapping("/project/{id}/cancelAll")
    public void cancelAll(@PathVariable Integer id, HttpServletResponse response,ModelMap model) throws IOException {
        projectMng.updateAllToDefault(id);
        response.getWriter().write("success");
    }

    @RequestMapping("/project/{id}/edit")
    public String edit(@PathVariable Integer id, HttpServletResponse response,ModelMap model) throws IOException {
        Project project=projectMng.findById(id);
        model.put("project",project);
        return "project/edit";
    }

    @RequestMapping("/project/{id}/update")
    public String update(@PathVariable Integer id,String basePath,String excludePath,String serverUploadPath,String remotePath, HttpServletResponse response,ModelMap model) throws IOException {
        Project project=projectMng.findById(id);
        project.setExcludePath(excludePath);
        project.setServerUploadPath(serverUploadPath);
        project.setRemotePath(remotePath);
        project.setBasePath(basePath);
        projectMng.update(project);
        return "redirect:/project/"+id;
    }



}

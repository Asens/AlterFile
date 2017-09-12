package cn.asens.controller;

import cn.asens.dao.UserDao;
import cn.asens.entity.User;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * Created by Asens on 2017/7/13
 */
@RestController
public class NoteAct {

    @Resource
    private UserDao userDao;

    @GetMapping("/service")
    public String service(){
        User user=userDao.findByaaId();
        return user.getName()+"--"+user.getId();
    }




}

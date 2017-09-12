package cn.asens;

import cn.asens.controller.SampleController;
import cn.asens.dao.UserDao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Asens on 2017/7/13
 */

@SpringBootApplication
@EnableJpaRepositories(basePackages  = "cn.asens.dao")
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}

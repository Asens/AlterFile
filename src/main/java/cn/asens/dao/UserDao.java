package cn.asens.dao;

import cn.asens.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * Created by Asens on 2017/7/16
 */
@Repository
public interface UserDao extends CrudRepository<User,Integer>,CustomUserDao{
    User save(User user);
    User findByName(String name);
}

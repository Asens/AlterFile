package cn.asens.dao.impl;

import cn.asens.dao.CustomUserDao;
import cn.asens.dao.UserDao;
import cn.asens.entity.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Asens on 2017/7/16
 */

@Repository
@Transactional
public class UserDaoImpl extends BaseDaoImpl implements CustomUserDao {


    @Override
    public User findByaaId() {
        return (User)getSession().createQuery("select u from User u where u.id=1")
                .uniqueResult();
    }
}

package cn.asens.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Asens on 2017/7/1
 */
public abstract class BaseDaoImpl {
    private SessionFactory sessionFactory;

    Session getSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Autowired
    protected void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }



}

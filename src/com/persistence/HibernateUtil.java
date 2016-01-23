package com.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
    static {
        try {
            sessionFactory = configureSessionFactory();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }
    
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
    
    private static SessionFactory configureSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        //configuration.configure();
        configuration.configure("com/persistence/hibernate.cfg.xml");
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();        
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }
    
}


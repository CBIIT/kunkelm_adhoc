/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.type.Type;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author mwkunkel
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) config file.
            //sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();

            sessionFactory = new Configuration().configure().buildSessionFactory();

        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

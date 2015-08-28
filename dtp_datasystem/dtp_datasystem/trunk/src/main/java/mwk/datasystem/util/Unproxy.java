/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

/**
 *
 * @author mwkunkel
 */
public class Unproxy {

  public static <T> T initializeAndUnproxy(T entity) {
    
    if (entity == null) {
      throw new NullPointerException("Entity passed for initialization is null");
    }

    Hibernate.initialize(entity);
    if (entity instanceof HibernateProxy) {
      entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
    }
    
    return entity;
    
  }
}

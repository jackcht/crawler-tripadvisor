package com.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


public abstract class DAO<T> {
	private Class<? extends T> model;
	
	public DAO(){
		super();
	}
	
	public DAO(Class<? extends T> model) {
		this();
		this.model = model;
	}
	
	@SuppressWarnings("unchecked")
	public T get(Object id){
		 Session session = null;
		 T result = null;
		 try{
			 session = HibernateUtil.getSession();
			 result = (T)session.createCriteria(this.model).add(Restrictions.eq("id",id)).uniqueResult();
		 }
		 catch(HibernateException e){
			 e.printStackTrace();
		 }
		 finally{
			 if (session != null)
				 session.close();
		 }
		 return result;
	 }

	 public void removeById(Object id){
		 Session session=null;
		 Transaction transact=null;
		 T object=this.get(id);
		 try{
			 session=HibernateUtil.getSession();
			 transact=session.beginTransaction();
			 session.delete(object);
			 transact.commit();
		 }
		 catch(HibernateException e){
			 if(transact!=null){
				 transact.rollback(); 
			 }
			 e.printStackTrace();    
		 }
		 finally{
			 if(session!=null){
				 session.close();
			 }
		 }
	 }

	 public void set(T object) {
		 Session session=null;
		 Transaction transact=null;
		 try{
			 session=HibernateUtil.getSession();
			 transact=session.beginTransaction();
			 session.save(object);
			 session.persist(object);
			 transact.commit();
			 session.refresh(object);
		 }
		 catch(HibernateException e){
			 if(transact!=null){
				 transact.rollback(); 
			 }
			 e.printStackTrace();    
		 }
		 finally{
			 if(session!=null){
				 session.close();
	            }
		 }   
	 } 

}

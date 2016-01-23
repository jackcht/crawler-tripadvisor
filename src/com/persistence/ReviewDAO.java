package com.persistence;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.model.Review;

public class ReviewDAO extends DAO<Review>{
	
	public ReviewDAO(){
		super(Review.class);
	}
	 
	 @SuppressWarnings("unchecked")
	public List<Review> getByDates(Date d1, Date d2){
		List<Review> reviews = null;
		Session session = null;
		Criteria criteria = null;
		try{
			session = HibernateUtil.getSession();
			criteria = session.createCriteria(Review.class)
					.add(Restrictions.ge("date", d1)).add(Restrictions.le("date", d2));
			reviews = criteria.list();
		}
		catch(HibernateException e){
			e.printStackTrace();
		}
		return reviews;
	}

}

package com.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
public class Review {
	
	private String _reviewId;			// such as r257068853
	private String _content = "";			// full text
	private Rating _rating;				// Enum: terrible/poor/average/verygood/excellent
	private Date _date;				// time stamp
	private String _title;				// 'small but fun'
	private String _author;				// Amna S
	private int _wordCount = 0;			// word count

	public Review(){
		super();
	}
	
	
	@Id
	@Column(name="ReviewId")
	public String getReviewId(){
		return _reviewId;
	}
	
	public void setReviewId(String reviewId){
		_reviewId = reviewId;
	}
	
	@Column(name="rating")
	public Rating getRating() {
		return _rating;
	}

	public void setRating(Rating rating){
		_rating = rating;
	}
	
	@Column(name="date")
	@Temporal(TemporalType.DATE)
	public Date getDate(){
		return _date;
	}
	
	public void setDate(Date date){
		_date = date;
	}
	
	@Column(name="title")
	public String getTitle(){
		return _title;
	}
	
	public void setTitle(String title){
		_title = title;
	}
	
	@Column(name="author")
	public String getAuthor(){
		return _author;
	}
	
	public void setAuthor(String author){
		_author = author;
	}
	
	@Column(name="wordcount")
	@Type(type="integer")
	public int getWordCount(){
		return _wordCount;
	}
	public void setWordCount(int count){
        _wordCount = count;
	}
	
	@Column(name="content")
	@Type(type="text")
	public String getContent(){
		return _content;
	}
	public void setContent(String content){
		_content = content;
	}
	
	
	public int calculateWordCount(){
		int count = 0;
		char ch[]= new char[_content.length()];
        for(int i = 0; i < _content.length(); i++)
        {
            ch[i] = _content.charAt(i);
            if( ((i > 0) && (ch[i] != ' ') && (ch[i-1] == ' ')) || ((ch[0] != ' ')&&(i == 0)) )
            	count++;
        }
        return count;
	}
	
	
	public boolean equals(Object o){
		Review review=(Review)o;
		return this.getReviewId().equals(review.getReviewId());
	}
	
	public int hashCode(){
		return this.getReviewId().hashCode();
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("Review: [ ");
		sb.append("ID: " + this.getReviewId() + " , ");
		sb.append("date: " + this.getDate().toString() + " , " );
		sb.append("rating: " + this.getRating().toString() + " , ");
		sb.append("title: '" + this.getTitle() + "'");
		sb.append(" ]");
		return sb.toString();
	}
	
}

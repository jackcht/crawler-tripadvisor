package com.model;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;


@Entity
public class Attraction {
	
	private String _attractionId;		// d2439664
	
	private String _name;				// Universal Studio Singapore

	private float _rating;				// like 4.5, 5.0, etc. 
	
	private int _numOfReviews;			// 6488
	
	private Date _crawlDate; 			// timestamp
	
	private Set<Review> _reviews;		// set of all the reviews
	
	private String _description;		// long text
	
	private String _imgSource; 			// representing pic
	
	private String _url; 				// original URL
	
	private String _location;			// location (one of the 4)
	
	private String _gpsLatitude;		// latitude;		1.281566
	
	private String _gpsLongtitude; 		// longitude;		103.86361
	
	
	public Attraction(){
		super();
		_reviews = new HashSet<Review>();
		_crawlDate = new Date(System.currentTimeMillis());
	}
	
	
	@Id
	@Column(name="attractionId")
	public String getAttractionId(){
		return _attractionId;
	}
	
	public void setAttractionId(String attractionId){
		_attractionId = attractionId;
	}
	
	@Column(name="attractionname")
	public String getName(){
		return _name;
	}
	
	public void setName(String name){
		_name = name;
	}
	
	@Column(name="rating")
	public float getRating(){
		return _rating;
	}
	
	public void setRating (float rating){
		_rating = rating;
	}
	
	@Column(name="numofreviews")
	public int getNumOfReviews(){
		return _numOfReviews;
	}
	
	public void setNumOfReviews(int num){
		_numOfReviews = num;
	}
	
	
	@Column(name="crawlDate")
	@Temporal(TemporalType.DATE)
	public Date getCrawlDate() {
		return _crawlDate;
	}

	public void setCrawlDate(Date crawlDate) {
		this._crawlDate = crawlDate;
	}
	
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="AttractionReviews",
			joinColumns=@JoinColumn(name="AttractionId"),
			inverseJoinColumns=@JoinColumn(name="ReviewId")
			)
	public Set<Review> getReviews() {
		return _reviews;
	}
	
	public void setReviews(Set<Review> reviews) {		//will not be used anyway
		_reviews = reviews;
	}
	
	
	@Column(name="description")
	@Type(type="text")
	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}
	
	@Column(name="imgsource")
	public String getImgSource() {
		return _imgSource;
	}

	public void setImgSource(String imgSource) {
		_imgSource = imgSource;
	}
	
	@Column(name="url")
	public String getUrl() {
		return _url;
	}

	public void setUrl(String url) {
		_url = url;
	}
	
	@Column(name="location")
	public String getLocation(){
		return _location;
	}
	
	public void setLocation(String location){
		_location = location;
	}
	
	@Column (name="latitude")
	public String getLatitude() {
		return _gpsLatitude;
	}
	
	public void setLatitude(String lat){
		_gpsLatitude = lat;
	}
	
	@Column (name="longtitude")
	public String getLongitude(){
		return _gpsLongtitude;
	}
	
	public void setLongitude(String lng){
		_gpsLongtitude = lng;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("Attraction: [ ");
		sb.append("ID: " + this.getAttractionId() + ", ");
		sb.append("Name: " + this.getName() + ", ");
		int numReviews = this.getReviews().size();
		sb.append("#reviews: " + numReviews);
		if ( numReviews > 0 ){
			sb.append("\r\n");
			for (Review r : this.getReviews()){
				sb.append( " \t" + r.toString() + " \r\n");
			}
		}
		sb.append(" ]");
		return sb.toString();
	}
}

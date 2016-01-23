package com.crawler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.model.Attraction;
import com.model.Review;
import com.parser.AttractionParser;
import com.parser.ReviewParser;

/**
 * [CHEN HAOTING] Modified from crawler-tripadvisor/src/it/thecrawlers/crawler/CrawlHandler.java from "https://github.com/dangrasso/crawler-tripadvisor"
 * @author CHT
 *
 */
public class CrawlHandler {
	
	private Map<String,Attraction> idAttractionMap;
	private Map<String,Review> idReviewMap;
	private Map<String,List<Review>> attractionIdToReviewMap;
	private AttractionParser attractionParser;
	private ReviewParser reviewParser;
	public int count=0;   				// num of attractions found
	public int completed=0; 			// counts of attractions which are completed parsing and written to DB
	
	public CrawlHandler(){
		this.idAttractionMap=new HashMap<String,Attraction>();
		this.idReviewMap = new HashMap<String,Review>();
		this.attractionIdToReviewMap = new HashMap<String,List<Review>>();
		this.attractionParser=new AttractionParser();
		this.reviewParser = new ReviewParser();
	}
	
	//idToAttraction
	public Map<String, Attraction> getIdAttractionMap() {
		return idAttractionMap;
	}
	public void setIdAttractionMap(Map<String, Attraction> mapping) {
		this.idAttractionMap = mapping;
	}
	public Attraction getAttractionById(String id){
		return this.idAttractionMap.get(id);
	}
	public void addNewAttraction(Attraction attraction){
		String id=attraction.getAttractionId();
		this.idAttractionMap.put(id, attraction);
	}
	
	//idToReview
	public Map<String, Review> getIdReviewMap(){
		return idReviewMap;
	}
	public void setIdReviewMap(Map<String, Review> mapping){
		this.idReviewMap = mapping;
	}
	public Review getReviewById(String id){
		return this.idReviewMap.get(id);
	}
	public void addNewReview(Review review){
		String id=review.getReviewId();
		this.idReviewMap.put(id, review);
	}
	
	//attractionIdToReviewMap				for saving the reviews to respective reviews 
	public Map<String, List<Review>> getAttractionIdToReviewMap(){
		return attractionIdToReviewMap;
	}
	public List<Review> getReviewsFromAttractionId(String attractionId){
		return this.attractionIdToReviewMap.get(attractionId);
	}
	public void addReviewsToAttraction(Review review, String attractionId){
		//this.attractionIdToReviewMap.put(attractionId, review);
		List<Review> list = this.attractionIdToReviewMap.get(attractionId);
		
		if (list == null){
			list = new LinkedList<Review>();
		}
		list.add(review);
		this.attractionIdToReviewMap.put(attractionId, list);
		
	}
	
	
	//
	public Review parseReview(String html,String path){
		return this.reviewParser.parseReview(html,path);
	}
	public Attraction parseAttraction(String html,String path){
		return this.attractionParser.parseAttraction(html, path);
	}
	public String parseAttractionIdFromPath(String path){
		String[] pathFields = path.split("-");
		return pathFields[2];
	}
	public String parseReviewIdFromPath(String path){
		String[] pathFields = path.split("-");
		return pathFields[3];
	}
	
	public boolean isReviewPage(String path){
		return this.reviewParser.isReviewPage(path);
	}
	public boolean isAttractionPage(String path){
		return this.attractionParser.isAttractionPage(path);
	}

}

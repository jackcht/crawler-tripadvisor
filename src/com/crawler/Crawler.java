package com.crawler;

import java.util.List;
import java.util.regex.Pattern;

import com.model.*;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Crawler extends WebCrawler{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	
	private CrawlHandler handler;
	
	public Crawler(){
		super();
	}
	public Crawler(CrawlHandler handler){
		super();
		this.handler=handler;
	}
	
	
     @Override
     public boolean shouldVisit(Page referringPage, WebURL url) {
    	 boolean result =false;
    	 String href = url.getURL().toLowerCase();
         
         if ( !FILTERS.matcher(href).matches() && href.startsWith("http://www.tripadvisor.com.sg")){
 			String path=url.getPath();
 			// pages of each attraction of the 4 locations
 			boolean attractionPage = path.startsWith("/Attraction_Review-g294263")			//Jurong   
 							|| path.startsWith("/Attraction_Review-g1644875")				//Pulau_Ubin
 							|| path.startsWith("/Attraction_Review-g294265") 				//Singapore 
 							|| path.startsWith("/Attraction_Review-g294264");				//Sentosa_Island
 							
 			// review page of the 4 locations
 			boolean reviewPage = path.startsWith("/ShowUserReviews-g294263")
 					|| path.startsWith("/ShowUserReviews-g1644875")
 					|| path.startsWith("/ShowUserReviews-g294265") 
 					|| path.startsWith("/ShowUserReviews-g294264");

 			//boolean attractionLocation = path.startsWith("Attractions-g294262");
 			//boolean sgAttraction = path.startsWith("/Tourism-g294262");				// Singapore Attractions
 			
 			result = attractionPage || reviewPage;
 				// ||sgAttraction || attractionLocation;
 		 }
         return result;
     }

     @Override
     public void visit(Page page) {

         String url = page.getWebURL().getURL();
         //System.out.println("Visiting URL: " + url);
         //log.info("Visiting URL: " + url);
         
         String path = page.getWebURL().getPath();
         
         try{
        	 if (handler.isAttractionPage(path) ){
      			if (page.getParseData() instanceof HtmlParseData) {
      				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
      				String html = htmlParseData.getHtml(); 
      				
      				String idAttraction = handler.parseAttractionIdFromPath(path);
      				Attraction attraction = handler.getAttractionById(idAttraction);
      				
      				if (attraction == null){
      					attraction = handler.parseAttraction(html,path);
      					handler.count++;
      					handler.addNewAttraction(attraction);
      				}
      				
      				String printAttraction = "Parsing Attraction ID: " + idAttraction + " \t Name: " + attraction.getName();
      				System.out.println(printAttraction);
      				//log.info(printAttraction);
      				
  					List<Review> list = handler.getReviewsFromAttractionId(idAttraction);
  					if (list != null){
						for (Review r: list){
							attraction.getReviews().add(r);
						}
  					}
      				
      			}
        	 }
        	 
        	 else if (handler.isReviewPage(path)){
        		 if (page.getParseData() instanceof HtmlParseData) {
       				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
       				String html = htmlParseData.getHtml(); 
       				
       				Review review = handler.parseReview(html,path);
       				if (review != null){
       					String printReview = review.toString();
       					System.out.println(printReview);
       					//log.info(printReview);
       					
           				handler.count++;
           				handler.addNewReview(review);
           				
           				String attractionId = handler.parseAttractionIdFromPath(path);
           				Attraction attraction = null;
           				try{
           					attraction = handler.getAttractionById(attractionId);
           					if (attraction != null){
    	       					if (! attraction.getReviews().contains(review)){
    	       						attraction.getReviews().add(review);
    	       					}
    	       					else{
    	       						throw new Exception ("Review already added to the Attraction !");
    	       					}
           					}
           					else{
           						handler.addReviewsToAttraction(review,attractionId);
           					}
           				}
           				catch (Exception e){
           					e.printStackTrace();
           				}
       				}
       				
        		 }
        	 }
         }
         catch (Exception e)
         {
        	 e.printStackTrace();
         }
         
     }
}

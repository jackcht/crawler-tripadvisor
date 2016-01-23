package com.parser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.model.Rating;
import com.model.Review;

public class ReviewParser {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public boolean isReviewPage(String path) {
		return path.startsWith("/ShowUserReviews");
	}
	
	public Review parseReview(String html, String path) {
		Document doc = Jsoup.parse(html);
		System.out.println("Parsing path: "+ path);
		log.info("Parsing path: "+path);
		
		String[] pathFields = path.split("-");	
		
		/*
		 * http://www.tripadvisor.com.sg/ShowUserReviews-g294264-d2439664-r257068853-Universal_Studios_Singapore-Sentosa_Island.html
		 * [0] /ShowUserReviews [1] g294264 [2] d2439664 [3] r257068853 
		 * [4] Universal_Studios_Singapore 
		 * [5] Sentosa_Island.html (final)
		 */
		String reviewID = pathFields[3];

		/*
		 * 	reviewId				// r257068853
		 *  content;				// full text
		 *  rating;					// Enum: terrible/poor/average/verygood/excellent
		 *  date;					// time stamp
		 *  title;					// 'small but fun'
		 *  author;					// Amna S
		 */
		 
		String content = "";
		Rating rating = null;
		Date date = null;
		String title = "";
		String author = "";
		
		
		try{
			Element detail  = doc.getElementById("review_"+reviewID.substring(1));
			
			if (detail.html().trim().isEmpty()){
	        	/*
				System.out.println("Calling Selenium library...");
	        	WebDriver driver = new HtmlUnitDriver(true);
	    		driver.get("www.tripadvisor.com.sg"+path);
	    		
	    		String htmlModified = (String) ((JavascriptExecutor)driver).executeScript("return document.documentElement.outerHTML");
	    		
	    		doc = Jsoup.parse(htmlModified);
	    		detail  = doc.getElementById("review_"+reviewID.substring(1));
	    		*/
				
				return null;
	        }
	        
			else{
			
				Document innerDoc = Jsoup.parseBodyFragment(detail.html());
				
				// author
				Elements username = innerDoc.getElementsByClass("username");
				author = username.first().text();
				
				// title
				Elements quote = innerDoc.getElementsByClass("quote");
				title = quote.first().text();
				
				// date
				Elements ratingDate = innerDoc.getElementsByClass("ratingDate");
				String[] array = ratingDate.first().text().split(" ");
				String dateStr = "";
				
				List<String> dateList = new ArrayList<String>(Arrays.asList(array));
				if (dateList.size() == 5){
					if (dateList.indexOf("NEW") == 4){
						dateList.remove(0);
						dateList.remove(3);
						array = dateList.toArray(new String[3]);
						dateStr = array[0] + " " + array[1] + " " + array[2];
					}
					else {
						throw new Exception("error in text of element class: ratingDate");
					}
				}
				else if (dateList.size() == 4){
					dateList.remove(0);
					array = dateList.toArray(new String[3]);
					dateStr = array[0] + " " + array[1] + " " + array[2];
				}
				else{
					throw new Exception("error in text of element class: ratingDate");
				}
				SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
				date = formatter.parse(dateStr);
				
				
				// Rating
				Elements sprite_rating = innerDoc.getElementsByClass("sprite-rating_s_fill");
				int ratingVal = Integer.parseInt(sprite_rating.first().attr("alt").substring(0, 1));
	
				if ((ratingVal < 1)||(ratingVal > 5))
					throw new Exception("Rating value: " + ratingVal + " is not correct (should be in the range of (1-5))");
	
				switch (ratingVal) {
					case 1:
						rating = Rating.TERRIBLE;
						break;
					case 2:
						rating = Rating.POOR;
						break;
					case 3:
						rating = Rating.AVERAGE;
						break;
					case 4:
						rating = Rating.VERYGOOD;
						break;
					case 5:
						rating = Rating.EXCELLENT;
						break;
				}
				
				// Content
				Elements entry = innerDoc.getElementsByClass("entry");
				content = entry.first().text();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Review newReview = new Review();
		newReview.setReviewId(reviewID);
		newReview.setAuthor(author);
		newReview.setTitle(title);
		newReview.setDate(date);
		newReview.setRating(rating);
		newReview.setContent(content);
		newReview.setWordCount(newReview.calculateWordCount());
		
		return newReview ;
	}

}

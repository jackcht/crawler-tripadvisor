package com.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.model.Attraction;


public class AttractionParser {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public boolean isAttractionPage(String path) {
		return path.startsWith("/Attraction_Review");
	}
	
	public Attraction parseAttraction(String html, String path){
		
		// html is the html text
		Document doc = Jsoup.parse(html);
		System.out.println("Parsing path: "+ path);
		log.info("Parsing path: "+path);

		// get from crawler4j.Page.getWebURL().getPath();
		// for 'http://www.example.com/sample.htm', domain will be 'sample.htm'
		String[] pathFields = path.split("-");	
		
		/*
		 * http://www.tripadvisor.com.sg/Attraction_Review-g294264-d2439664-Reviews-Universal_Studios_Singapore-Sentosa_Island.html
		 * http://www.tripadvisor.com.sg/Attraction_Review-g294264-d2439664-Reviews-or10-Universal_Studios_Singapore-Sentosa_Island.html#REVIEWS
		 * [0] /Attraction_Review [1] g294264 [2] d2439664 [3] Reviews 
		 * [4] * or10 [5/6] Universal_Studios_Singapore 
		 * [7/8] Sentosa_Island.html (final)
		 */
		String attractionID = pathFields[2];
		String name = pathFields[pathFields.length - 2].replace("_", " ");
		String locationID = pathFields[1];
		String location = "";
		switch(locationID){
			case "g294264": location = "Sentosa_Island";break;
			case "g294265": location = "Singapore"; break;
			case "g1644875": location = "Pulau_Ubin"; break;
			case "g294263" : location = "Jurong"; break;
		}
		
		// String locationID = pathFields[1];
		// String locationName = pathFields[pathFields.length - 1].substring(0, IndexOf(".") - 1); 
		// we have a one-to-many relationship for location-to-attractions 
		
		float rating = (float) 0.0;
		String numOfReviewsStr = "0";
		String latitude = "";
		String longtitude = "";
		String imgSrc = "";
		String description = "";
		
		Elements heading  = doc.getElementsByClass("headingWrapper");
		
		// rating
		try{
			Elements ratings = heading.select("img.sprite-rating_rr_fill");
			String rate = ratings.first().attr("alt");
			rating = Float.parseFloat(rate.substring(0, rate.indexOf(" ")));
		}
		catch (Exception e){
			//e.printStackTrace();
		}
		
		//number of reviews
		try{
			Elements numOfReviewsClass = heading.select("a.more");
			numOfReviewsStr = numOfReviewsClass.first().text();
			if (numOfReviewsStr.trim().endsWith("Reviews")||numOfReviewsStr.trim().endsWith("Review"))
				numOfReviewsStr = numOfReviewsStr.substring(0,numOfReviewsStr.indexOf(" "));
			numOfReviewsStr = numOfReviewsStr.replace(",", "");
		}
		catch (Exception e){
			//e.printStackTrace();
		}
		
		// longtitude & latitude
		try{
			Element nearby = doc.getElementById("NEARBY_TAB");
			Elements maps = nearby.children().select("div.mapContainer");
			latitude = maps.first().attr("data-lat");
			longtitude = maps.first().attr("data-lng");
		}
		catch (Exception e){
			//e.printStackTrace();
		}
		
		// imgSrc html address (NOT JPG)
		try{
			Element heroPhotoSrc = doc.getElementById("PHOTO_CELL_HERO_PHOTO");
			//Elements imgs = heroPhotoSrc.children().select("a[href]");
			//String imgSrclink = imgs.first().attr("href");
			
			if (heroPhotoSrc != null){
				Elements imgs1 = heroPhotoSrc.children().select("a[href]");
				
				String src = imgs1.first().attr("abs:href");
				
				doc = Jsoup.connect(src).get();
				Elements imgsrcs = doc.getElementsByClass("big_photo");
			
				imgSrc = imgsrcs.first().attr("src");
				
        	}
        	else{
        	}
		}
		catch (Exception e){
			//e.printStackTrace();
		}
		
		//description
		try{
			Element aboveFold = doc.getElementById("ABOVE_THE_FOLD");
			Elements listing_details = aboveFold.children().select("div.listing_details");
			description = listing_details.first().text().trim();	
		}
		catch (Exception e){
			//e.printStackTrace();
		}
				
		

		int numOfReviews = Integer.parseInt(numOfReviewsStr);
		
		Attraction attraction = new Attraction();
		attraction.setAttractionId(attractionID);
		attraction.setName(name);
		attraction.setNumOfReviews(numOfReviews);
		attraction.setRating(rating);
		attraction.setUrl("www.tripadvisor.com.sg"+path);
		
		attraction.setLatitude(latitude);
		attraction.setLongitude(longtitude);
		attraction.setDescription(description);
		attraction.setLocation(location);
		
		if (imgSrc.trim().isEmpty())
			attraction.setImgSource("");
		else
			attraction.setImgSource("www.tripadvisor.com.sg"+imgSrc);
		
		return attraction;
	}

}

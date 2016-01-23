package com.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.model.Attraction;

public class AttractionParserOriginal {
	public boolean isAttractionPage(String path) {
		return path.startsWith("/Attraction_Review");
	}
	
	public Attraction parseAttraction(String html, String path) throws Exception {
		
		// html is the html text
		Document doc = Jsoup.parse(html);
		System.out.println("Parsing path: "+ path);

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

		// String locationID = pathFields[1];
		// String locationName = pathFields[pathFields.length - 1].substring(0, IndexOf(".") - 1); 
		// we have a one-to-many relationship for location-to-attractions 
		
		
		String numOfReviewsStr = "0";
		float rating = (float) 0.0;
		//String superType = "";
		//String subType = "";
		//String ranking = "";
		//String address = "";
		//String phone = "";
		String imgSrc = "";
		String description = "";
		String latitude = "";
		String longtitude = "";
		//int[] distri = new int[5];
		
		try{
			Element nearby = doc.getElementById("NEARBY_TAB");
			Elements maps = nearby.children().select("div.mapContainer");
			latitude = maps.first().attr("data-lat");
			longtitude = maps.first().attr("data-lng");
			
			// TODO: replace the following by "select+CSS syntax" to read all needed elements
			Elements heading  = doc.getElementsByClass("headingWrapper");
			if (heading.size() == 1){
				Document innerDoc = Jsoup.parseBodyFragment(heading.html());
				// Elements heading = headings.children().select
				
				// rating
				Elements ratings = innerDoc.getElementsByClass("sprite-rating_rr_fill");
				String rate = ratings.first().attr("alt");
				rating = Float.parseFloat(rate.substring(0, rate.indexOf(" ")));
				
				
				//number of reviews
				Elements numOfReviewsClass = innerDoc.getElementsByClass("more");
				
				if (numOfReviewsClass.size() == 1){
					numOfReviewsStr = numOfReviewsClass.first().text();
					if (numOfReviewsStr.trim().endsWith("Reviews"))
						numOfReviewsStr = numOfReviewsStr.substring(0,numOfReviewsStr.indexOf(" "));
				}
				else{
					throw new Exception("Check the class of Element fetched, there are more than 1 element got");
				}
				
				/*
				// superType & subType
				Elements detail = innerDoc.getElementsByClass("detail");
				String type = detail.first().text();
				superType = type.substring(0, type.indexOf(","));
				subType = type.substring(type.indexOf(",")+1).trim();

				// ranking
				Elements rank_text = innerDoc.getElementsByClass("slim_ranking");
				ranking = rank_text.first().text();
				*/
				
				// change of the html section
				heading  = doc.getElementsByClass("above_the_fold");
				innerDoc = Jsoup.parseBodyFragment(heading.html());
				
				/*
				// address
				Elements format_address = innerDoc.getElementsByClass("format_address");
				String addr = format_address.first().text();
				address = addr.substring(addr.indexOf(":")+1).trim();

				//phone
				Elements phoneNumber = innerDoc.getElementsByClass("phoneNumber");
				String ph = phoneNumber.first().text();
				phone = ph.substring(ph.indexOf(":")+1).trim();
				
				//review distribution
				/*
				Elements barChart = innerDoc.getElementsByClass("barChart");
				String[] array = barChart.first().text().split(" "); 			//3 Excellent 3 Very good 4 Average 0 Poor 0 Terrible
				List<String> listStr = new ArrayList<String>(Arrays.asList(array));
				listStr.removeAll(Arrays.asList("good"));
				listStr.set(3, "Verygood");
				array = listStr.toArray(new String[array.length]);
				
				for (int i=0; i<5; i++){
					distri[i] = Integer.parseInt(array[i*2].replace(",", ""));
					//System.out.println(distri[i]);
				}
				*/
				
				// imgSrc html address (NOT JPG)
				Element heroPhotoSrc = doc.getElementById("PHOTO_CELL_HERO_PHOTO");
				Elements imgs = heroPhotoSrc.children().select("a[href]");
				imgSrc = imgs.first().attr("abs:href");
				
				//description
				Elements listing_details = innerDoc.getElementsByClass("listing_details");
				description = listing_details.first().text().trim();
				
			
			}
			
			else{
				throw new Exception("There are more than one headingWrapper class in html");
			}
				
				
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		

		int numOfReviews = Integer.parseInt(numOfReviewsStr);
		
		Attraction attraction = new Attraction();
		attraction.setAttractionId(attractionID);
		attraction.setName(name);
		attraction.setNumOfReviews(numOfReviews);
		attraction.setRating(rating);
		attraction.setUrl("www.tripadvisor.com.sg"+path);
		attraction.setImgSource(imgSrc);
		attraction.setLatitude(latitude);
		attraction.setLongitude(longtitude);
		
		/*
		attraction.setSupertype(superType);
		attraction.setSubType(subType);
		attraction.setRanking(ranking);
		attraction.setAddress(address);
		attraction.setPhone(phone);
		*/
		
		attraction.setDescription(description);
		
		return attraction;
	}
}

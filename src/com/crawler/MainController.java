/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crawler;

import com.persistence.AttractionDAO;
import com.persistence.ReviewDAO;
import com.model.Attraction;
import com.model.Review;

import java.util.Date;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [HAOTING CHEN] Modified from the crawler4j library code (crawler4j/examples/basic/BasicCrawlController.java)
 */

public class MainController {
	
	// Main function
	  public static void main(String[] args) throws Exception {
		  
		final Logger log = LoggerFactory.getLogger(MainController.class);
	    if (args.length != 2) {
	    	System.out.println("Needed parameters: ");
	    	System.out.println("\t rootFolder (it will contain intermediate crawl data)");
	    	System.out.println("\t numberOfCralwers (number of concurrent threads)");
	    	return;
	    }

	    /*
	     * crawlStorageFolder is a folder where intermediate crawl data is
	     * stored.
	     */
	    String crawlStorageFolder = args[0];

	    /*
	     * numberOfCrawlers shows the number of concurrent threads that should
	     * be initiated for crawling.
	     */
	    int numberOfCrawlers = Integer.parseInt(args[1]);

	    CrawlConfig config = new CrawlConfig();

	    config.setCrawlStorageFolder(crawlStorageFolder);

	    /*
	     * Be polite: Make sure that we don't send more than 1 request per
	     * second (1000 milliseconds between requests).
	     */
	    config.setPolitenessDelay(200);

	    /*
	     * You can set the maximum crawl depth here. The default value is -1 for
	     * unlimited depth
	     */
	    config.setMaxDepthOfCrawling(-1);

	    /*
	     * You can set the maximum number of pages to crawl. The default value
	     * is -1 for unlimited number of pages
	     */
	    config.setMaxPagesToFetch(-1);

	    /*
	     * Do you want crawler4j to crawl also binary data ?
	     * example: the contents of pdf, or the metadata of images etc
	     */
	    config.setIncludeBinaryContentInCrawling(false);

	    /*
	     * Do you need to set a proxy? If so, you can use:
	     * config.setProxyHost("proxyserver.example.com");
	     * config.setProxyPort(8080);
	     *
	     * If your proxy also needs authentication:
	     * config.setProxyUsername(username); config.getProxyPassword(password);
	     */
	    

	    /*
	     * This config parameter can be used to set your crawl to be resumable
	     * (meaning that you can resume the crawl from a previously
	     * interrupted/crashed crawl). Note: if you enable resuming feature and
	     * want to start a fresh crawl, you need to delete the contents of
	     * rootFolder manually.
	     */
	    config.setResumableCrawling(false);

	    /*
	     * Instantiate the controller for this crawl.
	     */
	    PageFetcher pageFetcher = new PageFetcher(config);
	    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	    CrawlControllerExtension controller = new CrawlControllerExtension(config, pageFetcher, robotstxtServer);

	    
	    /*
	     * For each crawl, you need to add some seed urls. These are the first
	     * URLs that are fetched and then the crawler starts following links
	     * which are found in these pages
	     */

	    //controller.addSeed("http://www.tripadvisor.com.sg/Tourism-g294262-Singapore-Vacations.html");
	    //controller.addSeed("http://www.tripadvisor.com.sg/Attractions-g294262-Activities-Singapore.html");
	    controller.addSeed("http://www.tripadvisor.com.sg/Attractions-g294265-Activities-Singapore.html");
	    controller.addSeed("http://www.tripadvisor.com.sg/Attractions-g294264-Activities-Sentosa_Island.html");
	    controller.addSeed("http://www.tripadvisor.com.sg/Attractions-g1644875-Activities-Pulau_Ubin.html");
	    controller.addSeed("http://www.tripadvisor.com.sg/Attractions-g294263-Activities-Jurong.html");
	    
	    Date startTime = new java.util.Date() ;
	    
		String start = "Crawling Initiated at: " + startTime.toString() + "\r\n"
				+ "Number of Crawlers: " + numberOfCrawlers + "\r\n"
				+ "Starting... ... ..." + "\r\n"
				+ "=============================================================";
		System.out.println(start);
		log.info(start); 
		
		long start_millSec= System.currentTimeMillis();
	    
	    /*
	     * Start the crawl. This is a blocking operation, meaning that your code
	     * will reach the line after this only when crawling is finished.
	     */
		CrawlHandler handler =new CrawlHandler();
	    controller.start(Crawler.class, numberOfCrawlers,handler);
	    
	    
	    /*
		 * Crawl completed, report elapsed time.
		 */
		Date endTime = new java.util.Date() ;
		long end_millSec= System.currentTimeMillis();
		
		int elapsed_secs = (int) (end_millSec - start_millSec) / 1000 ;
		int hours = elapsed_secs / 3600;
		int remainder = elapsed_secs % 3600;
		int minutes = remainder / 60;
		int seconds = remainder % 60; 
		String elapsed= hours + "h:" + minutes + "':" + seconds + "\"";
		
		String finish = "Crawling Finished" + "\r\n"
				+"Crawling Terminated at: " + endTime.toString()  + " ----- (Elapsed time : " + elapsed + " )" + "\r\n"
				+ "=============================================================";
		System.out.println(finish);
		log.info(finish);
		
		
		AttractionDAO attractionDao = new AttractionDAO();
		ReviewDAO reviewDao = new ReviewDAO();

		
		int reviewsNotFound = 0;
		int reviewsTotal = 0;
		long wordCount = 0;
		log.info("#################################################");
		for(Attraction i : handler.getIdAttractionMap().values()){
			for (Review r:i.getReviews()){
				reviewDao.set(r);
				wordCount += r.getWordCount();
			}
			attractionDao.set(i);
			log.info(i.toString());
			
			reviewsTotal+=i.getNumOfReviews();
			reviewsNotFound+=(i.getNumOfReviews()-i.getReviews().size());
			boolean outcome =(i.getNumOfReviews()!=i.getReviews().size());
			String test="META --- \tID:"+i.getAttractionId() + " Name: "+i.getName()+ 
					"\r\n ---------- Reviews missed ? "+ outcome + 
					"\t Number of reviews it has: "+i.getNumOfReviews()+
					"\t Number of reviews it read: "+i.getReviews().size();
			log.info(test);
			System.out.println(test);
			log.info("-------------------------------------------- \r\n");
			
		}
		log.info("#################################################");
		
		String reviewStatus="Links(Reviews) Not Found (Non-English reviews):  "+reviewsNotFound+ "\t Total Reviews: "+reviewsTotal;
		double perc=((double)(reviewsTotal-reviewsNotFound))/reviewsTotal;
		perc=perc*100;
		String percentage="Percentage of English reviews: "+perc+"%";
		log.info(reviewStatus);
		log.info(percentage);
		System.out.println(reviewStatus);
		System.out.println(percentage);
		log.info("#################################################");
		
		log.info("Total Attraction count: "+ handler.count);
		log.info("Total Word Count for " + reviewsTotal + " reviews: " + wordCount);
		
		// Wait for 30 seconds (can be changed)
	    Thread.sleep(30 * 1000);

	    // Send the shutdown request and then wait for finishing
	    controller.shutdown();
	    controller.waitUntilFinish();
	  }
}

/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package main.java.com.google.api.services.samples.youtube.cmdline;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Videos.List;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * Main class for the YouTube Data API command line sample.
 * Demonstrates how to make an authenticated API call using OAuth 2 helper classes.
 */
public class YouTubeSample {

	 /**
     * Global instance properties filename.
     */
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    /**
     * Global instance of the max number of videos we want returned.
     */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 10;

	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Global instance of Youtube object to make all API requests.
     */
    private static YouTube youtube;
    
    public static void main(String[] args) throws IOException {
		
    	Properties properties = new Properties();
        try {
            InputStream in =  YouTubeSample.class.getResourceAsStream("" + PROPERTIES_FILENAME);
            properties.load(in);

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }
        
        youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {

			@Override
			public void initialize(HttpRequest arg0) throws IOException {
				// TODO Auto-generated method stub
				
			}
        }).setApplicationName("youtubeTest").build();
        
        YouTube.Videos.List videos = youtube.videos().list("snippet");
        
        String apiKey = properties.getProperty("youtube.apikey");
        videos.setKey(apiKey);
        videos.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
        videos.setChart("mostPopular");
        
        VideoListResponse response = videos.execute();
        
       java.util.List<Video> videoList = response.getItems();
        
    	if(videoList != null){
    		prettyPrint(videoList.iterator());
    	}else{
    		System.out.println("There were  no results!");
    	}
        
	}
    
    private static void prettyPrint(Iterator<Video> videoIterator) {
		// TODO Auto-generated method stub
    	System.out.println("\n=============================================================");
        System.out.println("   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for most popular on youtube  ");
        System.out.println("=============================================================\n");
        
        if(!videoIterator.hasNext()){
        	System.out.println("There are no more results.");
        }
        
        while(videoIterator.hasNext()){
        	Video singleVideo = videoIterator.next();
        	
        	if(singleVideo.getKind().equals("youtube#video")){
        		System.out.println("ID : "+singleVideo.getId());
        		System.out.println("\nTitle : "+singleVideo.getSnippet().getTitle());
        		//System.out.println("\nDescription : "+singleVideo.getSnippet().getDescription());
        		System.out.println("\nThumbnail : "+singleVideo.getSnippet().getThumbnails().getDefault().getUrl());
        		System.out.println("\nChannel : "+singleVideo.getSnippet().getChannelTitle());
        		System.out.println("\n-----------------------------------------------------------");
        	}
        }

	}
 
}

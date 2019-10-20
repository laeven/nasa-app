package com.laeven.imageapp.services;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.laeven.imageapp.repositories.StorageService;
import com.laeven.imageapp.repositories.filestorage.ImageFileStorage;
import com.laeven.imageapp.utils.DateParser;
import com.laeven.imageapp.utils.ImageNotFoundException;

import java.io.FileReader;

@Service
public class PhotoRetriever {
  
  final String CONFIG_PATH = "/app/config.json";
  final String INIT_DATES_PATH = "/app/dates.txt";
  String apiToken;
  String photoURL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=%s&api_key=%s";
  StorageService storage;
  
  // PhotoRetrieve constructor intializes the API Token and 
  // the storage service. Currently only uses ImageFileStorage
  // but capable of expanding.
  public PhotoRetriever() throws IOException, ParseException {
    // Parse Config Files
    // Errors not handled as program must crash if config is wrong
    String storageDir;
    JSONParser parser = new JSONParser();
    JSONObject jsonObj = (JSONObject) parser.parse(new FileReader(CONFIG_PATH));
    apiToken = (String) jsonObj.get("apiToken");
    storageDir = (String) jsonObj.get("storageDir");
    storage = new ImageFileStorage(storageDir);

    // Initialize with init dates
    try {
      Scanner fReader = new Scanner(new File(INIT_DATES_PATH));
      while(fReader.hasNext()){
        String dateString = fReader.nextLine();
        Date parsedDate = new DateParser().parseDate(dateString);

        String photoURL = requestPhotoURL(parsedDate);
        boolean retrieved = retrieveImage(photoURL, parsedDate);
        if (!retrieved) {
          throw new ImageNotFoundException("No images found for inital date: "+dateString);
        }
        fReader.close();
      } 
    } catch(IOException e){
      e.printStackTrace();
    } catch(ImageNotFoundException e){
      e.printStackTrace();
    } 
  }

  // Function detects if image is cached before attempt
  // to retrieve directly from Nasa API
  public byte[] getImage(String dateString) throws java.text.ParseException{
    // Prepare date to usable format
    Date parsedDate = new DateParser().parseDate(dateString);
    String parsedDateString = new SimpleDateFormat("yyyy-MM-dd").format(parsedDate);
    String cachedImageName = String.format("image.%s.jpg", parsedDateString);
    
    // Check to see if image is already cached
    if (!storage.objectExists(cachedImageName)){
      String imageURL = requestPhotoURL(parsedDate);
      if (imageURL == null || imageURL.equals("")){
        return new byte[0];
      }
      retrieveImage(imageURL, parsedDate);
    };
    return storage.retrieve(cachedImageName);
  }

  private String requestPhotoURL(Date date){
    String requestURL = String.format(photoURL, new SimpleDateFormat("yyyy-MM-dd").format(date), apiToken);
    HttpGet request = new HttpGet(requestURL);
    try (CloseableHttpResponse response = HttpClients.createDefault().execute(request)){
      System.out.println(response.getStatusLine().toString());
      HttpEntity entity = response.getEntity();
      Header headers = entity.getContentType();
      System.out.println(headers);

      if (entity != null) {
        // return it as a String
        String result = EntityUtils.toString(entity);
        JSONParser p = new JSONParser();
        try {
          JSONObject json = (JSONObject) p.parse(result);
          JSONArray photos = (JSONArray) json.get("photos");
          JSONObject photo = (JSONObject) photos.get(0);
          return (String) photo.get("img_src");
        } catch(ParseException e){
          e.printStackTrace();
        }
      }
    } catch(ClientProtocolException e){
      e.printStackTrace();
    } catch(IOException e){
      e.printStackTrace();
    }
    return "";
  }

  // retrieveImage takes image URL and attempts to store 
  // utilizing the storage service. 
  private boolean retrieveImage(String imageURL, Date date){
    String imageID = String.format("image.%s.jpg", new SimpleDateFormat("yyyy-MM-dd").format(date));
    URL url;
    try{
      url = new URL(imageURL);
    }catch(MalformedURLException e){
      e.printStackTrace();
      return false;
    }

    try {
      InputStream in = new BufferedInputStream(url.openStream());
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      byte[] buf = new byte[1024];
      int n = 0;
      while (-1!=(n=in.read(buf)))
      {
        out.write(buf, 0, n);
      }
      out.close();
      in.close();
      byte[] response = out.toByteArray();

      return storage.store(imageID, response);
      
    } catch(IOException e){
      e.printStackTrace();
      return false;
    }
  }
}
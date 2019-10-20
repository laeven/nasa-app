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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.laeven.imageapp.utils.DateParser;

import java.io.FileReader;

public class PhotoRetriever {

  String apiToken;
  String photoURL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=%s&api_key=%s";

  public PhotoRetriever(String filename) throws FileNotFoundException, IOException, ParseException{
    JSONParser parser = new JSONParser();
    JSONObject jsonObj = (JSONObject) parser.parse(new FileReader(filename));
    apiToken = (String) jsonObj.get("apiToken");
  }

  // Function detects if image is cached before attempt
  // to retrieve directly from Nasa API
  public String getImage(String dateString){
    Date parsedDate = new DateParser().parseDate(dateString);

    String imageURL = requestPhotoURL(parsedDate);
    if (imageURL == null || imageURL.equals("")){
      return "ERROR: No image found.";
    }
    retrieveImage(imageURL, parsedDate);
    return "";
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

  private void retrieveImage(String imageURL, Date date){
    String outputFile = String.format("/tmp/image.%s.jpg", new SimpleDateFormat("yyyy-MM-dd").format(date));
    URL url;
    try{
      url = new URL(imageURL);
    }catch(MalformedURLException e){
      e.printStackTrace();
      return;
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
      
      FileOutputStream fos = new FileOutputStream(outputFile);
      fos.write(response);
      fos.close();
    } catch(IOException e){
      e.printStackTrace();
    }
  }
}
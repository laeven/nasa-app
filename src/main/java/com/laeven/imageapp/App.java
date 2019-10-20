package com.laeven.imageapp;

import java.io.FileNotFoundException;
import java.io.IOException;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.laeven.imageapp.services.PhotoRetriever;

import org.json.simple.parser.ParseException;


// @SpringBootApplication
public class App {

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
        // SpringApplication.run(App.class, args);
        PhotoRetriever a = new PhotoRetriever("config.json");
        a.getImage("02/27/17");
    }
}
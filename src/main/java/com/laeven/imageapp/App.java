package com.laeven.imageapp;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.json.simple.parser.ParseException;


@SpringBootApplication
public class App {

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
        SpringApplication.run(App.class, args);
    }
}
package com.laeven.imageapp.api;

import java.text.ParseException;

import com.laeven.imageapp.services.PhotoRetriever;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api/v1")
public class ImagesController {
    @Autowired
    private PhotoRetriever photoService;

    @RequestMapping(value = "/photo", method = RequestMethod.GET)
    public ResponseEntity<byte[]> Photo(@RequestParam(value="date", required = true) String date) {
        byte image[];
        try {
            // Retrieve image from the photoService
            // if date parse fails, return 400
            image = photoService.getImage(date);
        } catch( ParseException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new byte[0]);
        }
        if (image.length == 0){
            // Return a 404 on no image found/received
            throw new ResourceNotFoundException();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
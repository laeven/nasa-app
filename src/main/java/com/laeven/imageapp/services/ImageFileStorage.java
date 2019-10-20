package com.laeven.imageapp.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageFileStorage implements StorageService {
  // 
  String storageDir;

  public ImageFileStorage(String storageDir){
    this.storageDir = storageDir;
  }

  public boolean objectExists(String imageID){
    String filePath = String.format("%s/%s", storageDir, imageID);
    File imageFile = new File(filePath);
    return imageFile.exists();
  };

  public byte[] retrieve(String imageID){
    String filePath = String.format("%s/%s", storageDir, imageID);
    File imageFile = new File(filePath);
    byte[] imageBArr = new byte[(int) imageFile.length()];
    try{
      FileInputStream fstream = new FileInputStream(imageFile);
      fstream.read(imageBArr);
      fstream.close();
    } catch(IOException e){
      e.printStackTrace();
    }
    return imageBArr;
  };

  public boolean store(String imageID, byte[] data) {
    String filePath = String.format("%s/%s", storageDir, imageID);
    try{
      FileOutputStream fos = new FileOutputStream(filePath);
      fos.write(data);
      fos.close();
    } catch(IOException e){
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
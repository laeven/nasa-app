package com.laeven.imageapp.repositories;

// Description of StorageService than can
// use any type of backend to abstract away
// from service that needs it.
public interface StorageService {
  boolean objectExists(String id);
  byte[] retrieve(String imageID);
  boolean store(String imageID, byte[] data);
}

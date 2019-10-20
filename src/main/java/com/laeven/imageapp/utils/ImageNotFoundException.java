package com.laeven.imageapp.utils;

public class ImageNotFoundException extends RuntimeException {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ImageNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
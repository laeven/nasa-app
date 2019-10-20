package com.laeven.imageapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateParser{

  List<String> formatStrings = Arrays.asList("yyyy-MM-dd", "MM/dd/yy", "M d, yyyy", "MMM-dd-yyyy");

  public Date parseDate(String dateString){
    for (String pattern : formatStrings) {
      try {
        return new SimpleDateFormat(pattern).parse(dateString);
      } catch (ParseException pe) {}
    }
    return null;
  }
}
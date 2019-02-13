package com.raksit.example.order.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {

  public static String convertObjectToJsonString(Object object) throws Exception {
    return new ObjectMapper().writeValueAsString(object);
  }
}

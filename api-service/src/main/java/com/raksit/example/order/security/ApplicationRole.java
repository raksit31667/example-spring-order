package com.raksit.example.order.security;

import lombok.Getter;

public enum ApplicationRole {

  READ("Read"), WRITE("Write");

  @Getter
  private String value;

  ApplicationRole(String value) {
    this.value = value;
  }
}

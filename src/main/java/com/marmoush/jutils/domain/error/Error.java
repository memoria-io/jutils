package com.marmoush.jutils.domain.error;

public class Error extends Exception {
  public Error() { }

  public Error(String msg) {
    super(msg);
  }
}
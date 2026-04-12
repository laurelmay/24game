package me.laurelmay.game24.service.exception;

public class WrappedNoSuchMethodException extends RuntimeException {
  public WrappedNoSuchMethodException(NoSuchMethodException e) {
    super(e);
  }
}

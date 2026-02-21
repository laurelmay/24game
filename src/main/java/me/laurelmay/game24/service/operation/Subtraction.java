package me.laurelmay.game24.service.operation;

public class Subtraction extends Operation {
  public Subtraction(Operand lhs, Operand rhs) {
    super("-", false, lhs, rhs);
  }

  public int evaluate() {
    String errorMessage = String.format("Subtraction evaluation failed [lhs=%s, rhs=%s]", lhs, rhs);

    int difference = lhs.value() - rhs.value();
    if (difference < 0) {
      throw new IllegalArgumentException(errorMessage);
    }
    try {
      return Math.subtractExact(lhs.value(), rhs.value());
    } catch (ArithmeticException e) {
      throw new IllegalArgumentException(errorMessage, e);
    }
  }
}

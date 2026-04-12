package me.laurelmay.game24.service.operation;

public class Subtraction extends Operation {
  public Subtraction(Operand lhs, Operand rhs) {
    super("-", false, lhs, rhs);
  }

  public int evaluate() {
    try {
      int difference = Math.subtractExact(lhs.value(), rhs.value());
      if (difference < 0) {
        throw new IllegalArgumentException(String.format("Subtraction evaluation failed [lhs=%s, rhs=%s]", lhs, rhs));
      }
      return difference;
    } catch (ArithmeticException e) {
      String errorMessage = String.format("Subtraction evaluation failed [lhs=%s, rhs=%s]", lhs, rhs);
      throw new IllegalArgumentException(errorMessage, e);
    }
  }
}

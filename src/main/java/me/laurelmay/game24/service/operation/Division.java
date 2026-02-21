package me.laurelmay.game24.service.operation;

public class Division extends Operation {
  public Division(Operand lhs, Operand rhs) {
    super("/", false, lhs, rhs);
  }

  public int evaluate() {
    String errorMessage = String.format("Division evaluation failed: [lhs=%s, rhs=%s]", lhs, rhs);

    int numerator = lhs.value();
    int denominator = rhs.value();

    if (denominator == 0 || numerator % denominator != 0) {
      throw new IllegalArgumentException(errorMessage);
    }

    try {
      return Math.divideExact(numerator, denominator);
    } catch (ArithmeticException e) {
      throw new IllegalArgumentException(errorMessage, e);
    }
  }
}

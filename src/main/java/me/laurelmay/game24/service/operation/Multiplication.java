package me.laurelmay.game24.service.operation;

public class Multiplication extends Operation {
  public Multiplication(Operand lhs, Operand rhs) {
    super("*", true, lhs, rhs);
  }

  public int evaluate() {
    try {
      return Math.multiplyExact(lhs.value(), rhs.value());
    } catch (ArithmeticException e) {
      String errorMessage = String.format("Multiplication evaluation failed: [lhs=%s, rhs=%s]", lhs, rhs);
      throw new IllegalArgumentException(errorMessage, e);
    }
  }
}

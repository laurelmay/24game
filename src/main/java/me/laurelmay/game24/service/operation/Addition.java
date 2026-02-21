package me.laurelmay.game24.service.operation;

public class Addition extends Operation {
  public Addition(Operand lhs, Operand rhs) {
    super("+", true, lhs, rhs);
  }

  public int evaluate() {
    try {
      return Math.addExact(lhs.value(), rhs.value());
    } catch (ArithmeticException e) {
      throw new IllegalArgumentException(String.format("Addition evaluation failed: [lhs=%s, rhs=%s]", lhs, rhs), e);
    }
  }
}

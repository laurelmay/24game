package me.laurelmay.game24.service.operation;

public class Subtraction extends Operation {
  public Subtraction(Operand lhs, Operand rhs) {
    super("-", true, lhs, rhs);
  }

  public int evaluate() {
    int difference = lhs.value() - rhs.value();
    if (difference < 0) {
      throw new IllegalArgumentException("Subtraction evaluation failed: lhs " + lhs + ", rhs " + rhs);
    }
    return lhs.value() - rhs.value();
  }
}

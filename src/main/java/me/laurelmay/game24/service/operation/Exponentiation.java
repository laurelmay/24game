package me.laurelmay.game24.service.operation;

public class Exponentiation extends Operation {

  public Exponentiation(Operand lhs, Operand rhs) {
    super("^", false, lhs, rhs);
  }

  @Override
  public int evaluate() {
    String errorMessage = String.format("Exponentiation evaluation failed: [lhs=%s, rhs=%s]", lhs, rhs);
    if (this.lhs.value() < 0 || this.rhs.value() < 0) {
      throw new IllegalArgumentException(errorMessage);
    }
    try {
      return Math.powExact(lhs.value(), rhs.value());
    } catch (ArithmeticException ae) {
      throw new IllegalArgumentException(errorMessage, ae);
    }
  }
}

package me.laurelmay.game24.service.operation;

public class Exponentiation extends Operation {

  public Exponentiation(Operand lhs, Operand rhs) {
    super("^", false, lhs, rhs);
  }

  @Override
  public int evaluate() {
    if (this.lhs.value() < 0 || this.rhs.value() < 0) {
      throw new IllegalArgumentException("Exponentiation evaluation failed: lhs " + lhs + ", rhs " + rhs);
    }
    try {
      return Math.powExact(lhs.value(), rhs.value());
    } catch (ArithmeticException ae) {
      throw new IllegalArgumentException("Exponentiation evaluation failed: lhs " + lhs + ", rhs " + rhs, ae);
    }
  }
}

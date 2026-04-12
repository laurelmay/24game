package me.laurelmay.game24.service.operation;

public class Exponentiation extends Operation {

  public Exponentiation(Operand lhs, Operand rhs) {
    super("^", false, lhs, rhs);
  }

  @Override
  public int evaluate() {
    int base = this.lhs.value();
    int power = this.rhs.value();
    if (base < 0 || power < 0) {
      String errorMessage = String.format("Exponentiation evaluation failed: [lhs=%s, rhs=%s]", lhs, rhs);
      throw new IllegalArgumentException(errorMessage);
    }
    try {
      return Math.powExact(base, power);
    } catch (ArithmeticException ae) {
      String errorMessage = String.format("Exponentiation evaluation failed: [lhs=%s, rhs=%s]", lhs, rhs);
      throw new IllegalArgumentException(errorMessage, ae);
    }
  }
}

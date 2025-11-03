package me.laurelmay.game24.service.operation;

public class Logarithm extends Operation {
  public Logarithm(Operand lhs, Operand rhs) {
    super("log", false, lhs, rhs);
  }

  private static int log(int base, int antiLogarithm) {
    if (base <= 1 || antiLogarithm <= 0) {
      throw new IllegalArgumentException(
        "log evaluation failed: [base=" + base + ", antiLogarithm=" + antiLogarithm + "]");
    }
    int logarithm = (int) (Math.log(antiLogarithm) / Math.log(base));
    if (Math.powExact(base, logarithm) != antiLogarithm) {
      throw new IllegalArgumentException(
        "log evaluation failed: [base=" + base + ", antiLogarithm=" + antiLogarithm + "]");
    }
    return logarithm;
  }

  @Override
  public int evaluate() {
    return log(this.lhs.value(), this.rhs.value());
  }
}

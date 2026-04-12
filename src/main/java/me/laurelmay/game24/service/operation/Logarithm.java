package me.laurelmay.game24.service.operation;

import me.laurelmay.game24.service.util.CachedComputation;

public class Logarithm extends Operation {

  private final CachedComputation cachedResult;

  public Logarithm(Operand lhs, Operand rhs) {
    super("log", false, lhs, rhs);
    this.cachedResult = CachedComputation.computedWith(() -> log(this.lhs.value(), this.rhs.value()));
  }

  private static String createErrorMessage(int base, int antiLogarithm) {
    return String.format("log evaluation failed: [base=%d, antiLogarithm=%d]", base, antiLogarithm);
  }

  private static int log(int base, int antiLogarithm) {
    if (base <= 1 || antiLogarithm <= 0 || base > antiLogarithm) {
      throw new IllegalArgumentException(createErrorMessage(base, antiLogarithm));
    }
    int logarithm = (int) (Math.log(antiLogarithm) / Math.log(base));
    try {
      if (Math.powExact(base, logarithm) != antiLogarithm) {
        throw new IllegalArgumentException(createErrorMessage(base, antiLogarithm));
      }
    } catch (ArithmeticException e) {
      throw new IllegalArgumentException(createErrorMessage(base, antiLogarithm));
    }
    return logarithm;
  }

  @Override
  public int evaluate() {
    return cachedResult.get();
  }
}

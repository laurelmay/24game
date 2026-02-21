package me.laurelmay.game24.service.operation;

import me.laurelmay.game24.service.util.CachedComputation;

public class Root extends Operation {

  private final CachedComputation cachedResult;

  public Root(Operand lhs, Operand rhs) {
    super("√", false, lhs, rhs);
    this.cachedResult = CachedComputation.computedWith(() -> root(this.lhs.value(), this.rhs.value()));
  }

  private static int root(int radicand, int degree) {
    String errorMessage = String.format("Root evaluation failed: [radicand=%d, degree=%d]", radicand, degree);
    if (radicand <= 0 || degree <= 0) {
      throw new IllegalArgumentException(errorMessage);
    }
    int root = (int) Math.pow(radicand, 1.0 / degree);
    try {
      if (Math.powExact(root, degree) != radicand) {
        throw new IllegalArgumentException(errorMessage);
      }
    } catch (ArithmeticException e) {
      throw new IllegalArgumentException(errorMessage, e);
    }
    return root;
  }

  public int evaluate() {
    return cachedResult.get();
  }
}

package me.laurelmay.game24.service.operation;

public class Root extends Operation {
  public Root(Operand lhs, Operand rhs) {
    super("√", false, lhs, rhs);
  }

  private static int root(int radicand, int degree) {
    if (radicand <= 0 || degree <= 0) {
      throw new IllegalArgumentException("Root evaluation failed: [radicand=" + radicand + ", degree=" + degree + "]");
    }
    int root = (int) Math.pow(radicand, 1.0 / degree);
    if (Math.powExact(root, degree) != radicand) {
      throw new IllegalArgumentException("Root evaluation failed: [radicand=" + radicand + ", degree=" + degree + "]");
    }
    return root;
  }

  public int evaluate() {
    return root(this.lhs.value(), this.rhs.value());
  }
}

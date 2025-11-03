package me.laurelmay.game24.service.operation;

public class Addition extends Operation {
  public Addition(Operand lhs, Operand rhs) {
    super("+", true, lhs, rhs);
  }

  public int evaluate() {
    return lhs.value() + rhs.value();
  }
}

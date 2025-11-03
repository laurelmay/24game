package me.laurelmay.game24.service.operation;

public sealed interface Operand {
  int value();

  record Number(int value) implements Operand {
    @Override
    public String toString() {
      return Integer.toString(value);
    }
  }

  record Expression(Operation operation) implements Operand {
    @Override
    public int value() {
      return this.operation.evaluate();
    }

    @Override
    public String toString() {
      return this.operation.toString();
    }
  }
}

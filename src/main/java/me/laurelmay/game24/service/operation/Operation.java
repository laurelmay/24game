package me.laurelmay.game24.service.operation;

import java.util.Objects;

public abstract class Operation {
  protected final Operand lhs;
  protected final Operand rhs;
  private final String operator;
  private final boolean isCommutative;

  protected Operation(String operator, boolean isCommutative, Operand lhs, Operand rhs) {
    this.operator = operator;
    this.isCommutative = isCommutative;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public abstract int evaluate();

  public Operand getLhs() {
    return lhs;
  }

  public Operand getRhs() {
    return rhs;
  }

  public String getOperator() {
    return operator;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Operation operation = (Operation) o;
    if (this.isCommutative) {
      return Objects.equals(operator, operation.operator) &&
               ((Objects.equals(lhs, operation.lhs) && Objects.equals(rhs, operation.rhs)) ||
                  (Objects.equals(rhs, operation.lhs) && Objects.equals(lhs, operation.rhs)));
    }
    return Objects.equals(operator, operation.operator) &&
             Objects.equals(lhs, operation.lhs) &&
             Objects.equals(rhs, operation.rhs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lhs) ^ Objects.hash(rhs);
  }

  public String toString() {
    return "(" + lhs.toString() + " " + operator + " " + rhs.toString() + ")";
  }
}

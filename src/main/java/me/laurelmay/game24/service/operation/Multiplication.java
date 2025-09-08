package me.laurelmay.game24.service.operation;

public class Multiplication extends Operation {
    public Multiplication(Operand lhs, Operand rhs) {
        super("*", true, lhs, rhs);
    }

    public int evaluate() {
        return lhs.value() * rhs.value();
    }
}

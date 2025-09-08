package me.laurelmay.game24.service.operation;

public class Division extends Operation {
    public Division(Operand lhs, Operand rhs) {
        super("/", false, lhs, rhs);
    }

    public int evaluate() {
        int lhs = this.lhs.value();
        int rhs = this.rhs.value();

        if (rhs == 0 || lhs % rhs != 0) {
            throw new IllegalArgumentException("Division evaluation failed: lhs " + lhs + ", rhs " + rhs);
        }

        return lhs / rhs;
    }
}

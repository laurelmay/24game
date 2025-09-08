package me.laurelmay.game24.service.operation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdditionTest {
    @Test
    public void testEvaluate() {
        var addition = new Addition(new Operand.Number(1), new Operand.Number(2));
        assertEquals(3, addition.evaluate());
    }
}
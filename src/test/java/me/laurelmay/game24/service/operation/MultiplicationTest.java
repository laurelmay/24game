package me.laurelmay.game24.service.operation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultiplicationTest {
  @Test
  public void testEvaluate() {
    var multiplication = new Multiplication(new Operand.Number(4), new Operand.Number(2));
    assertEquals(8, multiplication.evaluate());
  }
}
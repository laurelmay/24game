package me.laurelmay.game24.service.operation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtractionTest {
  @Test
  public void testEvaluateThrowsErrorOnNegativeOutput() {
    var subtraction = new Subtraction(new Operand.Number(1), new Operand.Number(2));
    assertThrows(IllegalArgumentException.class, subtraction::evaluate);
  }
}
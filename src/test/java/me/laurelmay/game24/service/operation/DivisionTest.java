package me.laurelmay.game24.service.operation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DivisionTest {
  @Test
  public void testEvaluateErrorsOnRemainder() {
    var division = new Division(new Operand.Number(1), new Operand.Number(2));
    assertThrows(IllegalArgumentException.class, division::evaluate);
  }

  @Test
  public void testEvaluateErrorsOnZeroDenominator() {
    var division = new Division(new Operand.Number(1), new Operand.Number(0));
    assertThrows(IllegalArgumentException.class, division::evaluate);
  }

  @Test
  public void testEvaluateIsZeroWhenNumeratorIsZero() {
    var division = new Division(new Operand.Number(0), new Operand.Number(1));
    assertEquals(0, division.evaluate());
  }

  @Test
  public void testEvaluateWithNonZeroOperands() {
    var division = new Division(new Operand.Number(4), new Operand.Number(2));
    assertEquals(2, division.evaluate());
  }
}
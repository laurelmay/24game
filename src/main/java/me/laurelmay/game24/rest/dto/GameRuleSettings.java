package me.laurelmay.game24.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record GameRuleSettings(
  @Min(1) Integer targetValue,
  @Size(min = 1, max = 7) Set<Operation> allowedOperations
) {
  private static final int DEFAULT_TARGET_VALUE = 24;
  private static final Set<GameRuleSettings.Operation> DEFAULT_ALLOWED_OPERATIONS = Set.of(
    GameRuleSettings.Operation.ADDITION,
    GameRuleSettings.Operation.SUBTRACTION,
    GameRuleSettings.Operation.MULTIPLICATION,
    GameRuleSettings.Operation.DIVISION
  );

  public GameRuleSettings(Integer targetValue, Set<Operation> allowedOperations) {
    this.targetValue = (targetValue == null) ? DEFAULT_TARGET_VALUE : targetValue;
    this.allowedOperations = (allowedOperations == null || allowedOperations.isEmpty())
                             ? DEFAULT_ALLOWED_OPERATIONS
                             : Set.copyOf(allowedOperations);
  }

  public enum Operation {
    ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, EXPONENTIATION, LOGARITHM, ROOT
  }
}
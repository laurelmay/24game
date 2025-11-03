package me.laurelmay.game24.rest.dto;

import java.util.List;

public record SolvabilityCalculation(
  int targetValue,
  List<Integer> numbers,
  int combinationCount,
  int solvableCount,
  double percentSolvable
) {
}

package me.laurelmay.game24.service.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Combinatorics {
  private Combinatorics() {
  }

  public static <T> Set<List<T>> cartesianPower(Set<T> input, int power) {
    return cartesianPowerHelper(input, power).stream().map(List::copyOf).collect(Collectors.toUnmodifiableSet());
  }

  private static <T> Set<List<T>> cartesianPowerHelper(Set<T> input, int power) {
    Set<List<T>> result = new HashSet<>();
    if (power == 0) {
      result.add(new ArrayList<>());
      return result;
    }
    if (power == 1) {
      for (T element : input) {
        result.add(List.of(element));
      }
      return result;
    }

    Set<List<T>> previousPower = cartesianPower(input, power - 1);
    for (List<T> tuple : previousPower) {
      for (T element : input) {
        List<T> newTuple = new ArrayList<>(tuple);
        newTuple.add(element);
        result.add(newTuple);
      }
    }
    return result;
  }

  public static <T> Set<List<T>> permutations(List<T> input) {
    Set<List<T>> result = new HashSet<>(factorial(input.size()));
    permutationsHelper(new ArrayList<>(input), input.size(), result);
    return result.stream().map(List::copyOf).collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Computes all permutations of a given list using Heap's algorithm.
   */
  private static <T> void permutationsHelper(List<T> input, int k, Set<List<T>> result) {
    if (k == 1) {
      result.add(new ArrayList<>(input));
      return;
    }

    permutationsHelper(input, k - 1, result);
    for (int i = 0; i < k - 1; i++) {
      if (k % 2 == 0) {
        Collections.swap(input, i, k - 1);
      } else {
        Collections.swap(input, 0, k - 1);
      }
      permutationsHelper(input, k - 1, result);
    }
  }

  public static <T> Set<List<T>> combinationsWithRepetition(List<T> input, int k) {
    Set<List<T>> result = new HashSet<>();
    combinationsWithRepetitionHelper(input, k, 0, new ArrayList<>(), result);
    return result.parallelStream().map(List::copyOf).collect(Collectors.toUnmodifiableSet());
  }

  private static <T> void combinationsWithRepetitionHelper(List<T> input, int k, int startIdx, List<T> currentCombination, Set<List<T>> result) {
    if (k == 0) {
      result.add(new ArrayList<>(currentCombination));
      return;
    }
    for (int i = startIdx; i < input.size(); i++) {
      currentCombination.add(input.get(i));
      combinationsWithRepetitionHelper(input, k - 1, i, currentCombination, result);
      currentCombination.removeLast();
    }
  }

  private static int factorial(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("Only positive numbers are allowed for factorial calculation");
    }
    if (n == 0) {
      return 1;
    }
    return n * factorial(n - 1);
  }
}

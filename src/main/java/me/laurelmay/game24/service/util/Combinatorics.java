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

  public static <T> List<List<T>> cartesianPower(Set<T> input, int power) {
    List<T> elements = List.copyOf(input);
    int n = elements.size();

    int total = 1;
    for (int i = 0; i < power; i++) total *= n;

    List<List<T>> result = new ArrayList<>(total);
    for (int i = 0; i < total; i++) {
      List<T> tuple = new ArrayList<>(power);
      int index = i;
      for (int p = 0; p < power; p++) {
        tuple.add(elements.get(index % n));
        index /= n;
      }
      result.add(List.copyOf(tuple));
    }
    return List.copyOf(result);
  }

  public static <T extends Comparable<T>> List<List<T>> permutations(List<T> input) {
    List<T> current = new ArrayList<>(input);
    Collections.sort(current);

    List<List<T>> result = new ArrayList<>(factorial(input.size()));
    do {
      result.add(List.copyOf(current));
    } while (nextPermutation(current));

    return List.copyOf(result);
  }

  private static <T extends Comparable<T>> boolean nextPermutation(List<T> list) {
    int n = list.size();

    int i = n - 2;
    while (i >= 0 && list.get(i).compareTo(list.get(i + 1)) >= 0) i--;

    if (i < 0) return false;

    int j = n - 1;
    while (list.get(j).compareTo(list.get(i)) <= 0) j--;

    Collections.swap(list, i, j);
    Collections.reverse(list.subList(i + 1, n));
    return true;
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

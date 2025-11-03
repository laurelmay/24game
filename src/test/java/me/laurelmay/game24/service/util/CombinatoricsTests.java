package me.laurelmay.game24.service.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CombinatoricsTests {

  @Test
  public void testCartesianPower() {
    Set<List<Integer>> combinations = Combinatorics.cartesianPower(Set.of(1, 2, 3), 2);
    assertEquals(9, combinations.size());
    List<List<Integer>> expected = List.of(List.of(1, 1), List.of(1, 2), List.of(1, 3), List.of(2, 1), List.of(2, 2),
      List.of(2, 3), List.of(3, 1), List.of(3, 2), List.of(3, 3));
    System.out.println(combinations);
    assertTrue(combinations.containsAll(expected));
  }

  @Test
  public void testPermutations() {
    Set<List<Integer>> combinations = Combinatorics.permutations(List.of(1, 2, 3));
    assertEquals(6, combinations.size());
    List<List<Integer>> expected = List.of(List.of(1, 2, 3), List.of(1, 3, 2), List.of(2, 1, 3), List.of(2, 3, 1),
      List.of(3, 1, 2), List.of(3, 2, 1));
    assertTrue(combinations.containsAll(expected));
  }

  @Test
  public void testCombinationsWithRepetition() {
    Set<List<Integer>> combinations = Combinatorics.combinationsWithRepetition(List.of(1, 2, 3), 2);
    assertEquals(6, combinations.size());
    List<List<Integer>> expected = List.of(List.of(1, 1), List.of(1, 2), List.of(1, 3), List.of(2, 2), List.of(2, 3),
      List.of(3, 3));
    assertTrue(combinations.containsAll(expected));
  }
}
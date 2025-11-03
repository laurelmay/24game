package me.laurelmay.game24.service;

import me.laurelmay.game24.service.operation.Addition;
import me.laurelmay.game24.service.operation.Division;
import me.laurelmay.game24.service.operation.Multiplication;
import me.laurelmay.game24.service.operation.Operation;
import me.laurelmay.game24.service.operation.Subtraction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SolverServiceTest {

  @Autowired
  private SolverService solverService;

  private static final Set<Class<? extends Operation>> TEST_OPERATIONS = Set.of(
    Addition.class, Subtraction.class, Multiplication.class, Division.class
  );

  @Test
  public void testIsSolvableWithBasicInputsAndTarget24() {
    assertTrue(solverService.isSolvable(List.of(6, 6, 6, 6), 24, TEST_OPERATIONS));
    assertTrue(solverService.isSolvable(List.of(24, 1, 1, 1), 24, TEST_OPERATIONS));
    assertFalse(solverService.isSolvable(List.of(1, 1, 1, 1), 24, TEST_OPERATIONS));
  }

  @Test
  public void testIsSolvableWithBasicInputsAndTarget6() {
    assertTrue(solverService.isSolvable(List.of(6, 6, 6, 6), 6, TEST_OPERATIONS));
    assertFalse(solverService.isSolvable(List.of(24, 1, 1, 1), 6, TEST_OPERATIONS));
  }
}
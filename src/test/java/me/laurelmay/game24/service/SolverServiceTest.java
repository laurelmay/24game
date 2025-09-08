package me.laurelmay.game24.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SolverServiceTest {

    @Autowired
    private SolverService solverService;

    @Test
    public void testIsSolvableWithBasicInputsAndTarget24() {
        assertTrue(solverService.isSolvable(List.of(6, 6, 6, 6), 24));
        assertTrue(solverService.isSolvable(List.of(24, 1, 1, 1), 24));
        assertFalse(solverService.isSolvable(List.of(1, 1, 1, 1), 24));
    }

    @Test
    public void testIsSolvableWithBasicInputsAndTarget6() {
        assertTrue(solverService.isSolvable(List.of(6, 6, 6, 6), 6));
        assertFalse(solverService.isSolvable(List.of(24, 1, 1, 1), 6));
    }
}
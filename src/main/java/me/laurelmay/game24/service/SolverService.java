package me.laurelmay.game24.service;

import me.laurelmay.game24.service.operation.Addition;
import me.laurelmay.game24.service.operation.Division;
import me.laurelmay.game24.service.operation.Multiplication;
import me.laurelmay.game24.service.operation.Operand;
import me.laurelmay.game24.service.operation.Operation;
import me.laurelmay.game24.service.operation.Subtraction;
import me.laurelmay.game24.service.operation.*;
import me.laurelmay.game24.service.util.Combinatorics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SolverService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Set<Class<? extends Operation>> ALL_OPERATIONS = Set.of(Addition.class, Subtraction.class, Multiplication.class, Division.class);

    @Cacheable(value = "solutions", keyGenerator = "sortedListKeyGenerator")
    public Set<Operation> solve(List<Integer> numbers, int targetValue) {
        List<Operation> candidateSolutions = this.findCandidateSolutions(numbers);

        return candidateSolutions.parallelStream().filter(s -> isSolution(s, targetValue)).collect(Collectors.toUnmodifiableSet());
    }

    public boolean isSolvable(List<Integer> numbers, int targetValue) {
        List<Operation> candidateSolutions = this.findCandidateSolutions(numbers);

        return candidateSolutions.parallelStream().anyMatch(s -> isSolution(s, targetValue));
    }

    public boolean isSolution(Operation candidate, int targetValue) {
        try {
            return candidate.evaluate() == targetValue;
        } catch (IllegalArgumentException e) {
            logger.debug("Expression evaluation failed: [operation={}]", candidate);
            return false;
        }
    }

    private List<Operation> findCandidateSolutions(List<Integer> numbers) {
        Collection<List<Integer>> numberPermutations = Combinatorics.permutations(numbers);
        Collection<List<Class<? extends Operation>>> operationOrderings = Combinatorics.cartesianPower(ALL_OPERATIONS, 3);
        return numberPermutations.parallelStream()
                .flatMap(s -> operationOrderings.parallelStream().flatMap(o -> toOperations(s, o).stream())).toList();
    }

    private List<Operation> toOperations(List<Integer> numbers, List<Class<? extends Operation>> operations) {
        return Stream.of(
                toLeftToRightOperations(numbers, operations).stream(),
                toLeftToRightOperationsGroupingSecondAndThird(numbers, operations).stream(),
                toOuterPairOperations(numbers, operations).stream(),
                toRightToLeftOperations(numbers, operations).stream()
        )
                .flatMap(Function.identity())
                .toList();
    }

    private Optional<Operation> toOuterPairOperations(List<Integer> numbers, List<Class<? extends Operation>> operations) {
        try {
            Operand.Expression lhs = new Operand.Expression(findConstructor(operations.get(0)).newInstance(new Operand.Number(numbers.get(0)), new Operand.Number(numbers.get(1))));
            Operand.Expression rhs = new Operand.Expression(findConstructor(operations.get(2)).newInstance(new Operand.Number(numbers.get(2)), new Operand.Number(numbers.get(3))));
            return Optional.of(findConstructor(operations.get(1)).newInstance(lhs, rhs));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            return Optional.empty();
        }
    }

    private Optional<Operation> toLeftToRightOperations(List<Integer> numbers, List<Class<? extends Operation>> operations) {
        try {
            Operand a = new Operand.Expression(findConstructor(operations.get(0)).newInstance(new Operand.Number(numbers.get(0)), new Operand.Number(numbers.get(1))));
            Operand b = new Operand.Expression(findConstructor(operations.get(1)).newInstance(a, new Operand.Number(numbers.get(2))));
            return Optional.of(findConstructor(operations.get(2)).newInstance(b, new Operand.Number(numbers.get(3))));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            return Optional.empty();
        }
    }

    private Optional<Operation> toLeftToRightOperationsGroupingSecondAndThird(List<Integer> numbers, List<Class<? extends Operation>> operations) {
        try {
            Operand inner = new Operand.Expression(findConstructor(operations.get(1)).newInstance(new Operand.Number(numbers.get(1)), new Operand.Number(numbers.get(2))));
            Operand left = new Operand.Expression(findConstructor(operations.get(0)).newInstance(new Operand.Number(numbers.get(0)), inner));
            return Optional.of(findConstructor(operations.get(2)).newInstance(left, new Operand.Number(numbers.get(3))));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            return Optional.empty();
        }
    }

    private Optional<Operation> toRightToLeftOperations(List<Integer> numbers, List<Class<? extends Operation>> operations) {
        try {
            Operand inner = new Operand.Expression(findConstructor(operations.get(2)).newInstance(new Operand.Number(numbers.get(2)), new Operand.Number(numbers.get(3))));
            Operand right = new Operand.Expression(findConstructor(operations.get(1)).newInstance(new Operand.Number(numbers.get(1)), inner));
            return Optional.of(findConstructor(operations.get(0)).newInstance(new Operand.Number(numbers.get(0)), right));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            return Optional.empty();
        }
    }

    private Optional<Operation> toRightToLeftOperationGroupingSecondAndThird(List<Integer> numbers, List<Class<? extends Operation>> operations) {
        try {
            Operand inner = new Operand.Expression(findConstructor(operations.get(1)).newInstance(new Operand.Number(numbers.get(1)), new Operand.Number(numbers.get(2))));
            Operand right = new Operand.Expression(findConstructor(operations.get(2)).newInstance(inner, new Operand.Number(numbers.get(3))));
            return Optional.of(findConstructor(operations.get(0)).newInstance(new Operand.Number(numbers.get(0)), right));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            return Optional.empty();
        }
    }

    private Constructor<? extends Operation> findConstructor(Class<? extends Operation> operation) throws NoSuchMethodException {
        return operation.getConstructor(Operand.class, Operand.class);
    }
}

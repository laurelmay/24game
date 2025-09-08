package me.laurelmay.game24.rest;

import jakarta.validation.Valid;
import me.laurelmay.game24.Constants;
import me.laurelmay.game24.rest.dto.GameConfigurationResponse;
import me.laurelmay.game24.rest.dto.OperandResponse;
import me.laurelmay.game24.rest.dto.OperationResponse;
import me.laurelmay.game24.rest.dto.SolutionResponse;
import me.laurelmay.game24.rest.dto.Solvability;
import me.laurelmay.game24.rest.dto.SolvabilityCalculation;
import me.laurelmay.game24.rest.dto.SolveRequest;
import me.laurelmay.game24.service.GameConfiguration;
import me.laurelmay.game24.service.GameCreationService;
import me.laurelmay.game24.service.SolverService;
import me.laurelmay.game24.service.operation.Operand;
import me.laurelmay.game24.service.operation.Operation;
import me.laurelmay.game24.service.util.Combinatorics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class Solve24RestController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SolverService solverService;
    private final GameCreationService gameCreationService;

    @Autowired
    private Solve24RestController(SolverService solverService, GameCreationService gameCreationService) {
        this.solverService = solverService;
        this.gameCreationService = gameCreationService;
    }

    @PostMapping(path = "solve-24", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SolutionResponse game24(@RequestBody @Valid SolveRequest solveRequest) {
        logger.info("Solve request: {}", solveRequest);
        Set<Operation> solutions = solverService.solve(solveRequest.numbers(), Constants.TARGET_VALUE);
        return toSolutionResponse(solutions);
    }

    @GetMapping(value = "random-game", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameConfigurationResponse randomGame(@RequestParam(defaultValue = "SOLVABLE") Solvability solvability, @RequestParam(defaultValue = "24") int targetValue) {
        GameConfiguration gameConfiguration = gameCreationService.generateRandomGame(solvability, targetValue);
        return new GameConfigurationResponse(gameConfiguration.numbers(), solvability, gameConfiguration.solvability());
    }

    @GetMapping(value = "percent-solvable", produces = MediaType.APPLICATION_JSON_VALUE)
    public SolvabilityCalculation percentSolvable(@RequestParam(defaultValue = "24") int targetValue) {
        List<Integer> inputSpace = Constants.NUMBERS;
        Collection<List<Integer>> inputOrderings = Combinatorics.combinationsWithRepetition(inputSpace, Constants.GAME_INTEGER_COUNT);
        int solvable = (int) inputOrderings.parallelStream().filter(i -> solverService.isSolvable(i, targetValue)).count();
        return new SolvabilityCalculation(targetValue, inputSpace, inputOrderings.size(), solvable, solvable / (double) inputOrderings.size());
    }

    private SolutionResponse toSolutionResponse(Set<Operation> operations) {
        return new SolutionResponse(operations.stream().map(this::toOperationResponse).collect(Collectors.toUnmodifiableSet()));
    }

    private OperationResponse toOperationResponse(Operation operation) {
        return new OperationResponse(toOperandResponse(operation.getLhs()), toOperandResponse(operation.getRhs()), operation.getOperator());
    }

    private OperandResponse toOperandResponse(Operand operand) {
        return switch (operand) {
            case Operand.Number i -> new OperandResponse.Number(i.value());
            case Operand.Expression e -> new OperandResponse.Expression(toOperationResponse(e.operation()));
        };
    }
}

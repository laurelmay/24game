package me.laurelmay.game24.rest;

import jakarta.validation.Valid;
import me.laurelmay.game24.rest.dto.GameConfigurationResponse;
import me.laurelmay.game24.rest.dto.GameCreationRequest;
import me.laurelmay.game24.rest.dto.GameRuleSettings;
import me.laurelmay.game24.rest.dto.OperandResponse;
import me.laurelmay.game24.rest.dto.OperationResponse;
import me.laurelmay.game24.rest.dto.SolutionResponse;
import me.laurelmay.game24.rest.dto.Solvability;
import me.laurelmay.game24.rest.dto.SolveRequest;
import me.laurelmay.game24.service.GameConfiguration;
import me.laurelmay.game24.service.GameCreationService;
import me.laurelmay.game24.service.SolverService;
import me.laurelmay.game24.service.operation.Addition;
import me.laurelmay.game24.service.operation.Division;
import me.laurelmay.game24.service.operation.Exponentiation;
import me.laurelmay.game24.service.operation.Logarithm;
import me.laurelmay.game24.service.operation.Multiplication;
import me.laurelmay.game24.service.operation.Operand;
import me.laurelmay.game24.service.operation.Operation;
import me.laurelmay.game24.service.operation.Root;
import me.laurelmay.game24.service.operation.Subtraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
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
    Set<Operation> solutions = solverService.solve(solveRequest.numbers(),
      solveRequest.gameRuleSettings().targetValue(),
      toOperations(solveRequest.gameRuleSettings().allowedOperations()));
    return toSolutionResponse(solutions);
  }

  @PostMapping(value = "random-game", produces = MediaType.APPLICATION_JSON_VALUE)
  public GameConfigurationResponse randomGame(@RequestBody @Valid GameCreationRequest gameCreationRequest) {
    Solvability solvability = gameCreationRequest.solvability();
    GameConfiguration gameConfiguration = gameCreationService.generateRandomGame(solvability,
      gameCreationRequest.gameRuleSettings().targetValue(),
      gameCreationRequest.minimumNumber(),
      gameCreationRequest.maximumNumber(),
      toOperations(gameCreationRequest.gameRuleSettings().allowedOperations()));
    return new GameConfigurationResponse(gameConfiguration.numbers(), solvability, gameConfiguration.solvability());
  }

  private SolutionResponse toSolutionResponse(Set<Operation> operations) {
    return new SolutionResponse(
      operations.stream().map(this::toOperationResponse).collect(Collectors.toUnmodifiableSet()));
  }

  private OperationResponse toOperationResponse(Operation operation) {
    return new OperationResponse(toOperandResponse(operation.getLhs()), toOperandResponse(operation.getRhs()),
      operation.getOperator());
  }

  private OperandResponse toOperandResponse(Operand operand) {
    return switch (operand) {
      case Operand.Number i -> new OperandResponse.Number(i.value());
      case Operand.Expression e -> new OperandResponse.Expression(toOperationResponse(e.operation()));
    };
  }

  private Set<Class<? extends Operation>> toOperations(Set<GameRuleSettings.Operation> operations) {
    return operations.stream().map(o -> switch (o) {
      case ADDITION -> Addition.class;
      case SUBTRACTION -> Subtraction.class;
      case MULTIPLICATION -> Multiplication.class;
      case DIVISION -> Division.class;
      case EXPONENTIATION -> Exponentiation.class;
      case LOGARITHM -> Logarithm.class;
      case ROOT -> Root.class;
    }).collect(Collectors.toUnmodifiableSet());
  }
}

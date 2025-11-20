package me.laurelmay.game24.service;

import me.laurelmay.game24.Constants;
import me.laurelmay.game24.Game24ConfigurationProperties;
import me.laurelmay.game24.rest.dto.Solvability;
import me.laurelmay.game24.service.operation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

@Service
public class GameCreationService {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final SolverService solverService;
  private final Game24ConfigurationProperties game24ConfigurationProperties;
  private final ThreadLocal<RandomGenerator> randomGenerator;

  @Autowired
  public GameCreationService(SolverService solverService, Game24ConfigurationProperties game24ConfigurationProperties) {
    this.solverService = solverService;
    this.game24ConfigurationProperties = game24ConfigurationProperties;
    RandomGeneratorFactory<RandomGenerator> randomGeneratorFactory = RandomGeneratorFactory.all()
                                                                       .filter(
                                                                         rgf -> !rgf.name().equals("SecureRandom"))
                                                                       .filter(Predicate.not(
                                                                         RandomGeneratorFactory::isDeprecated))
                                                                       .filter(rgf -> rgf.equidistribution() >=
                                                                                        Constants.GAME_INTEGER_COUNT)
                                                                       .max(Comparator.comparingInt(
                                                                         RandomGeneratorFactory<RandomGenerator>::stateBits))
                                                                       .orElse(RandomGeneratorFactory.of("Random"));
    logger.info("Using random generator: {}", randomGeneratorFactory.name());
    this.randomGenerator = ThreadLocal.withInitial(randomGeneratorFactory::create);
  }

  public GameConfiguration generateRandomGame(Solvability desiredSolvability, int targetValue, int minimumValue, int maximumValue,
                                              Set<Class<? extends Operation>> allowedOperations) {
    List<Integer> numbers;
    Solvability actualSolvability;
    for (int i = 0; i < game24ConfigurationProperties.maximumGameGenerationAttempts(); i++) {
      numbers = this.generateNumbers(minimumValue, maximumValue);
      actualSolvability = solverService.isSolvable(numbers, targetValue, allowedOperations)
                          ? Solvability.SOLVABLE
                          : Solvability.UNSOLVABLE;
      if (desiredSolvability == Solvability.UNKNOWN || desiredSolvability == actualSolvability) {
        return new GameConfiguration(actualSolvability, numbers);
      }
    }

    throw new RuntimeException("Failed to generate a game within maximum attempts");
  }

  private List<Integer> generateNumbers(int minimumValue, int maximumValue) {
    RandomGenerator generator = randomGenerator.get();
    return generator.ints(minimumValue, maximumValue + 1)
             .limit(Constants.GAME_INTEGER_COUNT)
             .boxed()
             .toList();
  }
}

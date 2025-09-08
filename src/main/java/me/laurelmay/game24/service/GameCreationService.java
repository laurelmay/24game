package me.laurelmay.game24.service;

import me.laurelmay.game24.Constants;
import me.laurelmay.game24.rest.dto.Solvability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

@Service
public class GameCreationService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SolverService solverService;
    private final ThreadLocal<RandomGenerator> randomGenerator;

    @Autowired
    public GameCreationService(SolverService solverService) {
        this.solverService = solverService;
        RandomGeneratorFactory<RandomGenerator> randomGeneratorFactory = RandomGeneratorFactory.all()
                .filter(rgf -> !rgf.name().equals("SecureRandom"))
                .filter(Predicate.not(RandomGeneratorFactory::isDeprecated))
                .filter(rgf -> rgf.equidistribution() >= Constants.GAME_INTEGER_COUNT)
                .max(Comparator.comparingInt(RandomGeneratorFactory<RandomGenerator>::stateBits))
                .orElse(RandomGeneratorFactory.of("Random"));
        logger.info("Using random generator: {}", randomGeneratorFactory.name());
        this.randomGenerator = ThreadLocal.withInitial(randomGeneratorFactory::create);
    }

    public GameConfiguration generateRandomGame(Solvability desiredSolvability, int targetValue) {
        List<Integer> numbers;
        Solvability actualSolvability;
        do {
            numbers = this.generateNumbers();
            actualSolvability = solverService.isSolvable(numbers, targetValue) ? Solvability.SOLVABLE : Solvability.UNSOLVABLE;
        } while (desiredSolvability != Solvability.UNKNOWN && desiredSolvability != actualSolvability);

        return new GameConfiguration(actualSolvability, numbers);
    }

    private List<Integer> generateNumbers() {
        RandomGenerator generator = randomGenerator.get();
        return generator.ints(Constants.MINIMUM_VALUE, Constants.MAXIMUM_VALUE + 1).limit(Constants.GAME_INTEGER_COUNT).boxed().toList();
    }
}

package me.laurelmay.game24.service;

import me.laurelmay.game24.Constants;
import me.laurelmay.game24.service.util.Combinatorics;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.List;

@Service
public class SolutionCacheWarmerService implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final SolverService solverService;

    @Autowired
    public SolutionCacheWarmerService(SolverService solverService) {
        this.solverService = solverService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        Collection<List<Integer>> inputOrderings = Combinatorics.combinationsWithRepetition(Constants.NUMBERS, Constants.GAME_INTEGER_COUNT);
        logger.info("Pre-warming solution cache with all single-digit inputs [count={}]", inputOrderings.size());
        inputOrderings.parallelStream().forEach(i -> solverService.solve(i, Constants.TARGET_VALUE));
        logger.info("Pre-warming is complete");
    }
}

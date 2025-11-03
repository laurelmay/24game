package me.laurelmay.game24.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import me.laurelmay.game24.Constants;

import java.util.List;

public record GameConfigurationResponse(
  @Min(Constants.GAME_INTEGER_COUNT) @Max(Constants.GAME_INTEGER_COUNT) @NotNull List<@Min(0) Integer> numbers,
  @NotNull Solvability requestedSolvability,
  @NotNull Solvability actualSolvability
) {
}

package me.laurelmay.game24.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import me.laurelmay.game24.Constants;

import java.util.List;

public record SolveRequest(
  @Size(min = Constants.GAME_INTEGER_COUNT, max = Constants.GAME_INTEGER_COUNT) List<@Min(0) Integer> numbers,
  GameRuleSettings gameRuleSettings
) {
  public SolveRequest(@Size(min = Constants.GAME_INTEGER_COUNT, max = Constants.GAME_INTEGER_COUNT) List<@Min(0) Integer> numbers, GameRuleSettings gameRuleSettings) {
    this.numbers = numbers;
    this.gameRuleSettings = (gameRuleSettings == null) ? new GameRuleSettings(null, null) : gameRuleSettings;
  }
}

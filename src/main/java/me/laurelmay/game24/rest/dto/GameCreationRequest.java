package me.laurelmay.game24.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import me.laurelmay.game24.Constants;

public record GameCreationRequest(
  Solvability solvability,
  @Min(0) Integer minimumNumber,
  @Max(48) Integer maximumNumber,
  GameRuleSettings gameRuleSettings
) {
  public GameCreationRequest(Solvability solvability, @Min(0) @Max(100) Integer minimumNumber, @Min(0) @Max(100) Integer maximumNumber, GameRuleSettings gameRuleSettings) {
    this.solvability = solvability == null ? Solvability.SOLVABLE : solvability;
    this.minimumNumber = minimumNumber == null ? Constants.MINIMUM_VALUE : minimumNumber;
    this.maximumNumber = maximumNumber == null ? Constants.MAXIMUM_VALUE : maximumNumber;
    this.gameRuleSettings = (gameRuleSettings == null) ? new GameRuleSettings(null, null) : gameRuleSettings;
  }
}

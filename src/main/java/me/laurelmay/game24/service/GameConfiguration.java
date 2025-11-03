package me.laurelmay.game24.service;

import me.laurelmay.game24.rest.dto.Solvability;

import java.util.List;

public record GameConfiguration(Solvability solvability, List<Integer> numbers) {
}

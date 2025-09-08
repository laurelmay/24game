package me.laurelmay.game24.service;

import java.util.List;

import me.laurelmay.game24.rest.dto.Solvability;

public record GameConfiguration(Solvability solvability, List<Integer> numbers) { }

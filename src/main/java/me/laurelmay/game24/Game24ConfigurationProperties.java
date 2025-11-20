package me.laurelmay.game24;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "game24")
public record Game24ConfigurationProperties(int maximumGameGenerationAttempts) { }

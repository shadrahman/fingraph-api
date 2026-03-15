package io.github.shadrahman.fingraph.model;

public record Transaction(
        String id,
        Double amount,
        String description,
        Category category,
        boolean isSubscription
) {}

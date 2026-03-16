package io.github.shadrahman.fingraph.model;

public record Transaction(
        String id,
        Long amount,
        String description,
        Category category,
        boolean isSubscription
) {
}

package io.github.shadrahman.fingraph.model;

public record Transaction(
        String id,
        Long amount,
        TransactionType type,
        String description,
        Category category,
        boolean isSubscription
) {
}

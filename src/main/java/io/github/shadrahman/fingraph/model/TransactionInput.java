package io.github.shadrahman.fingraph.model;

public record TransactionInput(
        String accountId,
        Double amount,
        TransactionType type,
        String description,
        Category category
) {
}

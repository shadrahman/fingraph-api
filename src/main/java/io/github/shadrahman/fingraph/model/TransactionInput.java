package io.github.shadrahman.fingraph.model;

public record TransactionInput(
        String accountId,
        Long amount,
        String description,
        Category category
) {
}

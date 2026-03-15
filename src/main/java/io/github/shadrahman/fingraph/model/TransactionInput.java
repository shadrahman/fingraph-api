package io.github.shadrahman.fingraph.model;

public record TransactionInput(
        String accountId,
        Double amount,
        String description,
        Category category
) {}

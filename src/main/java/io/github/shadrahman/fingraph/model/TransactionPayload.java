package io.github.shadrahman.fingraph.model;

public record TransactionPayload(
        Transaction transaction,
        Account account,
        boolean success
) {}

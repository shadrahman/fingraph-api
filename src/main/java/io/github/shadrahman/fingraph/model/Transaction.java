package io.github.shadrahman.fingraph.model;

public record Transaction(
        String id,
        Double amount,
        String description,
        String category
) {
}

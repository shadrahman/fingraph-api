package io.github.shadrahman.fingraph.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TransactionInput(
        @NotBlank(message = "Account ID is required")
        String accountId,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        Double amount,

        @NotNull(message = "Transaction type is required")
        TransactionType type,

        @NotBlank(message = "Description cannot be empty")
        @Size(max = 100, message = "Description is too long")
        String description,

        @NotNull(message = "Category is required")
        Category category
) {
}

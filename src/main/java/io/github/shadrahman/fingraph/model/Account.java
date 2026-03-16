package io.github.shadrahman.fingraph.model;

import java.util.List;

public record Account(
        String id,
        String userId,
        String name,
        Long balance,
        List<Transaction> history
) {
}

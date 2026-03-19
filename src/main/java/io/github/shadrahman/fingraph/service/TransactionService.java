package io.github.shadrahman.fingraph.service;

import io.github.shadrahman.fingraph.model.Category;
import io.github.shadrahman.fingraph.model.Transaction;
import io.github.shadrahman.fingraph.model.TransactionType;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    public static final long CENTS_FACTOR = 100L;
    private static final Map<String, Category> MERCHANT_MAP = Map.ofEntries(
            Map.entry("coles", Category.FOOD),
            Map.entry("woolworths", Category.FOOD),
            Map.entry("aldi", Category.FOOD),
            Map.entry("iga", Category.FOOD),
            Map.entry("woolworths caltex", Category.TRANSPORT),
            Map.entry("7-eleven", Category.TRANSPORT),
            Map.entry("ampol", Category.TRANSPORT),
            Map.entry("bp", Category.TRANSPORT),
            Map.entry("opal", Category.TRANSPORT),
            Map.entry("uber", Category.TRANSPORT),
            Map.entry("binge", Category.SUBSCRIPTION),
            Map.entry("stan", Category.SUBSCRIPTION),
            Map.entry("kayo", Category.SUBSCRIPTION),
            Map.entry("netflix", Category.SUBSCRIPTION),
            Map.entry("disney plus", Category.SUBSCRIPTION),
            Map.entry("mcdonalds", Category.FOOD),
            Map.entry("hungry jacks", Category.FOOD),
            Map.entry("menulog", Category.FOOD),
            Map.entry("door dash", Category.FOOD),
            Map.entry("kmart", Category.SHOPPING),
            Map.entry("target", Category.SHOPPING),
            Map.entry("bunnings", Category.SHOPPING)
    );

    public Transaction createNewTransaction(Long amount, TransactionType type, String description,
                                            Category userSuggestedCategory) {
        Category finalCategory = determineCategory(description, userSuggestedCategory);
        boolean isSubscription = finalCategory == Category.SUBSCRIPTION;

        return new Transaction(
                UUID.randomUUID().toString(),
                amount,
                type,
                description,
                finalCategory,
                isSubscription
        );
    }

    /**
     * Assigns a Category based on the description.
     */
    public Category determineCategory(String description, Category userSuggested) {
        if (description == null || description.isBlank()) {
            return (userSuggested != null) ? userSuggested : Category.MISCELLANEOUS;
        }

        String cleanDesc = description.toLowerCase();

        return MERCHANT_MAP.entrySet().stream()
                           // Sort by key length descending so "Woolworths Caltex" matches before "Woolworths"
                           .sorted((e1, e2) -> Integer.compare(e2.getKey().length(), e1.getKey().length()))
                           .filter(entry -> cleanDesc.contains(entry.getKey()))
                           .map(Map.Entry::getValue)
                           .findFirst()
                           .orElse(userSuggested != null ? userSuggested : Category.MISCELLANEOUS);
    }
}

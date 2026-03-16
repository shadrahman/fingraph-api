package io.github.shadrahman.fingraph.service;

import io.github.shadrahman.fingraph.model.Category;
import io.github.shadrahman.fingraph.model.Transaction;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    // A list of "Keywords" that flag a subscription
    private static final List<String> SUBSCRIPTION_KEYWORDS = List.of(
            "netflix", "spotify", "hulu", "gym", "prime", "apple", "icloud", "chatgpt", "amazon prime"
    );

    public Transaction createNewTransaction(Double amount, String description, Category userSuggestedCategory) {
        boolean isSubscription = detectSubscription(description);
        Category finalCategory = determineCategory(userSuggestedCategory, isSubscription);

        return new Transaction(
                UUID.randomUUID().toString(),
                amount,
                description,
                finalCategory,
                isSubscription
        );
    }

    /**
     * Checks if a description implies a recurring cost.
     */
    public boolean detectSubscription(String description) {
        if (description == null || description.isBlank()) {
            return false;
        }

        String cleanDesc = description.toLowerCase();
        return SUBSCRIPTION_KEYWORDS.stream().anyMatch(cleanDesc::contains);
    }

    /**
     * Assigns a Category based on the description.
     */
    private Category determineCategory(Category suggested, boolean isSubscription) {
        if (isSubscription) {
            return Category.SUBSCRIPTION;
        }
        if (suggested != null) {
            return suggested;
        }
        return Category.MISCELLANEOUS;
    }
}

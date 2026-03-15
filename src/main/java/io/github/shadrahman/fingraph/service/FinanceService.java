package io.github.shadrahman.fingraph.service;

import io.github.shadrahman.fingraph.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceService {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public FinanceService() {
        accounts.put("acc-1", new Account("acc-1", "Main Checking", 1500.00, new ArrayList<>()));
        accounts.put("acc-2", new Account("acc-2", "Savings", 5000.00, new ArrayList<>()));
    }

    public Account getAccountById(String id) {
        return accounts.get(id);
    }

    public TransactionPayload addTransaction(TransactionInput input) {
        Account account = accounts.get(input.accountId());

        if (account != null) {
            var isDetectedSub = isSubscription(input.description());
            var finalCategory = isDetectedSub ? Category.SUBSCRIPTION : input.category();

            var newTransaction = new Transaction(UUID.randomUUID().toString(), input.amount(), input.description(), finalCategory, isDetectedSub);
            var updatedHistory = account.history();
            updatedHistory.add(0, newTransaction); // Add to the top of the list
            var updatedBalance = account.balance() - input.amount();
            var updatedAccount = new Account(account.id(), account.name(), updatedBalance, updatedHistory);

            accounts.put(account.id(), updatedAccount);

            return new TransactionPayload(newTransaction, updatedAccount, true);
        }
        return new TransactionPayload(null, null, false);
    }

    /**
     * Encapsulates the business rules for subscription detection.
     */
    private boolean isSubscription(String description) {
        if (description == null) {
            return false;
        }

        String desc = description.toLowerCase();
        return desc.contains("netflix") ||
                desc.contains("spotify") ||
                desc.contains("gym") ||
                desc.contains("amazon prime");
    }
}

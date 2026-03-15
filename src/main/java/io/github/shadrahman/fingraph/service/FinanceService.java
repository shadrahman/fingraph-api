package io.github.shadrahman.fingraph.service;

import io.github.shadrahman.fingraph.model.Account;
import io.github.shadrahman.fingraph.model.Transaction;
import io.github.shadrahman.fingraph.model.TransactionPayload;
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

    public TransactionPayload addTransaction(String accountId, Double amount, String desc) {
        Account account = accounts.get(accountId);

        if (account != null) {
            var newTransaction = new Transaction(UUID.randomUUID().toString(), amount, desc, "ENTERTAINMENT");
            var updatedBalance = account.balance() - amount;
            var updatedHistory = account.history();
            updatedHistory.add(0, newTransaction); // Add to the top of the list
            var updatedAccount = new Account(accountId, account.name(), updatedBalance, updatedHistory);

            accounts.put(accountId, updatedAccount);

            return new TransactionPayload(newTransaction, updatedAccount, true);
        }
        return new TransactionPayload(null, null, false);
    }
}

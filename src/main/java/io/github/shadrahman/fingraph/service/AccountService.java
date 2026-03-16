package io.github.shadrahman.fingraph.service;

import io.github.shadrahman.fingraph.model.Account;
import io.github.shadrahman.fingraph.model.TransactionInput;
import io.github.shadrahman.fingraph.model.TransactionPayload;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final TransactionService transactionService;
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public AccountService(TransactionService transactionService) {
        accounts.put("acc-1", new Account("acc-1", "usr-123", "Main Checking", 1500.00, new ArrayList<>()));
        accounts.put("acc-2", new Account("acc-2", "usr-123", "Savings", 5000.00, new ArrayList<>()));
        this.transactionService = transactionService;
    }

    public List<Account> getAccountsByUserId(String userId) {
        return accounts.values().stream()
                       .filter(account -> userId.equals(account.userId()))
                       .toList();
    }

    public Account getAccountById(String id) {
        return accounts.get(id);
    }

    public TransactionPayload addTransaction(TransactionInput input) {
        Account account = accounts.get(input.accountId());

        if (account != null) {
            var newTransaction = transactionService.createNewTransaction(input.amount(), input.description(), input.category());
            var updatedHistory = new ArrayList<>(account.history());
            updatedHistory.add(0, newTransaction); // Add to the top of the list
            var updatedBalance = account.balance() - input.amount();
            var updatedAccount = new Account(account.id(), account.userId(), account.name(), updatedBalance, updatedHistory);

            accounts.put(account.id(), updatedAccount);

            return new TransactionPayload(newTransaction, updatedAccount, true);
        }
        return new TransactionPayload(null, null, false);
    }
}

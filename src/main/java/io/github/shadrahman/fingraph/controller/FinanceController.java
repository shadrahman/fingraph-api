package io.github.shadrahman.fingraph.controller;

import io.github.shadrahman.fingraph.model.Account;
import io.github.shadrahman.fingraph.model.Transaction;
import io.github.shadrahman.fingraph.model.TransactionPayload;
import io.github.shadrahman.fingraph.model.User;
import io.github.shadrahman.fingraph.service.FinanceService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FinanceController {
    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @QueryMapping
    public User me() {
        return new User("u-1", "John Doe", "john@example.com");
    }

    @SchemaMapping(typeName = "User", field = "accounts")
    public List<Account> getAccounts(User user) {
        return List.of(
                new Account("a-1", "Checking", 1500.50, new ArrayList<>()),
                new Account("a-1", "Savings", 5000.00, new ArrayList<>())
        );
    }

    @QueryMapping
    public Account accountById(@Argument String id) {
        return financeService.getAccountById(id);
    }

    @SchemaMapping(typeName = "Account", field = "history")
    public List<Transaction> getHistory(Account account, @Argument Integer limit) {
        List<Transaction> allTransactions = account.history();

        if (limit != null && limit > 0 && limit < allTransactions.size()) {
            return allTransactions.subList(0, limit);
        }

        return allTransactions;
    }

    @MutationMapping
    public TransactionPayload createTransaction(@Argument String accountId,
                                                @Argument Double amount,
                                                @Argument String description) {
        return financeService.addTransaction(accountId, amount, description);
    }
}

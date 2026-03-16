package io.github.shadrahman.fingraph.controller;

import io.github.shadrahman.fingraph.model.Account;
import io.github.shadrahman.fingraph.model.Transaction;
import io.github.shadrahman.fingraph.service.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import static io.github.shadrahman.fingraph.service.TransactionService.CENTS_FACTOR;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @QueryMapping
    public Account accountById(@Argument String id) {
        return accountService.getAccountById(id);
    }

    @SchemaMapping(typeName = "Account", field = "balance")
    public Double getBalanceInDollars(Account account) {
        return account.balance() / (double) CENTS_FACTOR;
    }

    @SchemaMapping(typeName = "Account", field = "history")
    public List<Transaction> getHistory(Account account, @Argument Integer limit) {
        var allTransactions = account.history();

        if (limit != null && limit > 0 && limit < allTransactions.size()) {
            return allTransactions.subList(0, limit);
        }

        return allTransactions;
    }
}

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

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @QueryMapping
    public Account accountById(@Argument String id) {
        return accountService.getAccountById(id);
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

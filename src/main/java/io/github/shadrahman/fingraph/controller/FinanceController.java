package io.github.shadrahman.fingraph.controller;

import io.github.shadrahman.fingraph.model.TransactionInput;
import io.github.shadrahman.fingraph.model.TransactionPayload;
import io.github.shadrahman.fingraph.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FinanceController {
    private final AccountService accountService;

    @MutationMapping
    public TransactionPayload createTransaction(@Argument TransactionInput input) {
        return accountService.addTransaction(input);
    }
}

package io.github.shadrahman.fingraph.controller;

import io.github.shadrahman.fingraph.model.TransactionInput;
import io.github.shadrahman.fingraph.model.TransactionPayload;
import io.github.shadrahman.fingraph.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import static io.github.shadrahman.fingraph.service.TransactionService.CENTS_FACTOR;

@Controller
@RequiredArgsConstructor
public class FinanceController {
    private final AccountService accountService;

    @MutationMapping
    public TransactionPayload createTransaction(@Argument TransactionInput input) {
        long amount = Math.round(input.amount() * CENTS_FACTOR);

        var updatedAccount = accountService.addTransaction(
                input.accountId(),
                amount,
                input.type(),
                input.description(),
                input.category());

        return updatedAccount != null ?
                new TransactionPayload(updatedAccount.history().get(0), updatedAccount, true) :
                new TransactionPayload(null, null, false);
    }
}

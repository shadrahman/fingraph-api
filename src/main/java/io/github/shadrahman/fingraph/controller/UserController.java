package io.github.shadrahman.fingraph.controller;

import io.github.shadrahman.fingraph.model.Account;
import io.github.shadrahman.fingraph.model.CategoryTotal;
import io.github.shadrahman.fingraph.model.User;
import io.github.shadrahman.fingraph.service.AccountService;
import io.github.shadrahman.fingraph.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import static io.github.shadrahman.fingraph.service.TransactionService.CENTS_FACTOR;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AccountService accountService;

    @QueryMapping
    public User me() {
        return new User("usr-123", "John Doe", "john@example.com");
    }

    @SchemaMapping(typeName = "User", field = "accounts")
    public List<Account> accounts(User user) {
        return accountService.getAccountsByUserId(user.id());
    }

    @SchemaMapping(typeName = "User", field = "totalNetWorth")
    public Double totalNetWorth(User user) {
        return userService.calculateNetWorth(user.id()) / (double) CENTS_FACTOR;
    }

    @SchemaMapping(typeName = "User", field = "monthlySubscriptionCommitted")
    public Double monthlySubscriptionCommitted(User user) {
        return userService.calculateMonthlySubscriptionCommitted(user.id()) / (double) CENTS_FACTOR;
    }

    @SchemaMapping(typeName = "User", field = "monthlySpending")
    public List<CategoryTotal> monthlySpending(User user) {
        return userService.getMonthlySpending(user.id())
                          .entrySet().stream()
                          .map(entry -> new CategoryTotal(entry.getKey(), entry.getValue() / (double) CENTS_FACTOR))
                          .toList();
    }
}

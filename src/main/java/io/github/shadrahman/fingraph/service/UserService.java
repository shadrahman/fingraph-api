package io.github.shadrahman.fingraph.service;

import io.github.shadrahman.fingraph.model.Account;
import io.github.shadrahman.fingraph.model.CategoryTotal;
import io.github.shadrahman.fingraph.model.Transaction;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AccountService accountService;

    public Double calculateNetWorth(String userId) {
        return accountService.getAccountsByUserId(userId).stream()
                             .mapToDouble(Account::balance)
                             .sum();
    }

    public Double calculateMonthlySubscriptionCommitted(String userId) {
        return accountService.getAccountsByUserId(userId).stream()
                             .flatMap(account -> account.history().stream())
                             .filter(Transaction::isSubscription)
                             .mapToDouble(Transaction::amount)
                             .sum();
    }

    public List<CategoryTotal> getMonthlySpending(String userId) {
        // 1. Get all accounts belonging to the user
        // 2. Flatten all transactions from those accounts into one list
        // 3. Group them by category and sum the amounts
        return accountService.getAccountsByUserId(userId).stream()
                             .flatMap(account -> account.history().stream())
                             .collect(Collectors.groupingBy(
                                     Transaction::category,
                                     Collectors.summingDouble(Transaction::amount)
                             ))
                             .entrySet().stream()
                             .map(entry -> new CategoryTotal(entry.getKey(), entry.getValue()))
                             .toList();
    }
}

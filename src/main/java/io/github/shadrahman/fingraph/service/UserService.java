package io.github.shadrahman.fingraph.service;

import io.github.shadrahman.fingraph.model.Account;
import io.github.shadrahman.fingraph.model.Category;
import io.github.shadrahman.fingraph.model.CategoryTotal;
import io.github.shadrahman.fingraph.model.Transaction;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AccountService accountService;

    public Long calculateNetWorth(String userId) {
        return accountService.getAccountsByUserId(userId).stream()
                             .mapToLong(Account::balance)
                             .sum();
    }

    public Long calculateMonthlySubscriptionCommitted(String userId) {
        return accountService.getAccountsByUserId(userId).stream()
                             .flatMap(account -> account.history().stream())
                             .filter(Transaction::isSubscription)
                             .mapToLong(Transaction::amount)
                             .sum();
    }

    public Map<Category, Long> getMonthlySpending(String userId) {
        // 1. Get all accounts belonging to the user
        // 2. Flatten all transactions from those accounts into one list
        // 3. Group them by category and sum the amounts
        return accountService.getAccountsByUserId(userId).stream()
                             .flatMap(account -> account.history().stream())
                             .collect(Collectors.groupingBy(
                                     Transaction::category,
                                     Collectors.summingLong(Transaction::amount)
                             ));
    }
}

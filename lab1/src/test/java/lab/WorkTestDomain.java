package lab;

import lab.domain.AccountType;
import lab.domain.Bank;
import lab.domain.History;
import lab.domain.IBankAccount;
import lab.dto.UserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkTestDomain {
    static Stream<Arguments> BankTestsCreateAccount () {
        return Stream.of(
                Arguments.of(0L, AccountType.CREDIT),
                Arguments.of(0L, AccountType.DEBIT),
                Arguments.of(0L, AccountType.DEPOSIT)
        );
    }

    @ParameterizedTest
    @MethodSource("BankTestsCreateAccount")
    public void bankCreatingAccountTests(long id, AccountType type) {
        UserRepository userRepository = new UserRepository();
        Bank bank = new Bank(userRepository, "1", "1", 0L, null);
        bank.createUser("d", "d", Optional.of(1L), "h".describeConstable());
        bank.addBankAccount(id, type);
        List<IBankAccount> account = userRepository.getUserAccounts().get(id);
        Optional<IBankAccount> resType = account.stream().filter(iBankAccount -> iBankAccount.getAccountType() == type).findFirst();
        assertThat(resType.get().getAccountType()).isEqualTo(type);
    }

    static Stream<Arguments> BankTestsPutOperation () {
        return Stream.of(
                Arguments.of(0, 0,  1, AccountType.CREDIT),
                Arguments.of(0, 0,  1, AccountType.DEPOSIT),
                Arguments.of(0, 0,  1, AccountType.DEBIT)
        );
    }

    @ParameterizedTest
    @MethodSource("BankTestsPutOperation")
    public void bankOperationPutMoneyTests(long id, long accountId, long amount, AccountType accountType) {
        History history = new History(new ArrayList<>());
        UserRepository userRepository = new UserRepository();
        Bank bank = new Bank(userRepository, "1", "1", 0L, null);
        bank.createUser("d", "d", Optional.of(1L), "h".describeConstable());
        bank.addBankAccount(id, accountType);
        bank.putMoney(id, accountId, amount, history);
        assertThat(userRepository.getUserAccounts().get(id).get(0).getBalance()).isEqualTo(BigDecimal.valueOf(1));
    }

    static Stream<Arguments> BankTestsWithdrawOperation () {
        return Stream.of(
                Arguments.of(0, 0,  2, 1, AccountType.CREDIT),
                Arguments.of(0, 0,  2, 1, AccountType.DEBIT)
        );
    }

    @ParameterizedTest
    @MethodSource("BankTestsWithdrawOperation")
    public void bankOperationWithdrawMoneyTests(long id, long accountId, long toPut, long amount, AccountType accountType) {
        History history = new History(new ArrayList<>());
        UserRepository userRepository = new UserRepository();
        Bank bank = new Bank(userRepository, "1", "1", 0L, null);
        bank.createUser("d", "d", Optional.of(1L), "h".describeConstable());
        bank.addBankAccount(id, accountType);
        bank.putMoney(id, accountId, toPut, history);
        bank.withdrawMoney(id, accountId, amount, history);
        assertThat(userRepository.getUserAccounts().get(id).get(0).getBalance()).isEqualTo(BigDecimal.valueOf(1.0));
    }

    static Stream<Arguments> BankTestsTransferOperation () {
        return Stream.of(
                Arguments.of(0, 1, 0, 0, 2, 4, AccountType.CREDIT),
                Arguments.of(0, 1, 0, 0, 2, 4, AccountType.DEBIT)
        );
    }

    @ParameterizedTest
    @MethodSource("BankTestsTransferOperation")
    public void bankOperationTransferMoneyTests(long id, long toId, long accountId, long toAccountId, long amount, long baseBalance, AccountType accountType) {
        History history = new History(new ArrayList<>());
        UserRepository userRepository = new UserRepository();
        Bank bank = new Bank(userRepository, "1", "1", 0L, null);
        bank.createUser("d", "d", Optional.of(1L), "h".describeConstable());
        bank.createUser("gh", "d", Optional.of(1L), "h".describeConstable());
        bank.addBankAccount(id, accountType);
        bank.addBankAccount(toId, accountType);
        bank.putMoney(id, accountId, baseBalance, history);
        bank.putMoney(toId, toAccountId, baseBalance, history);
        bank.transferMoney(id, accountId, toId, toAccountId, amount, history);
        assertThat(userRepository.getUserAccounts().get(id).get(0).getBalance()).isEqualTo(BigDecimal.valueOf(2.0));
        assertThat(userRepository.getUserAccounts().get(toId).get(0).getBalance()).isEqualTo(BigDecimal.valueOf(6));
    }

}

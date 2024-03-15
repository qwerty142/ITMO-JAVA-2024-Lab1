package lab.domain;

import lab.dto.BankAdmin;
import lab.dto.User;
import lab.dto.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Log4j2
public class Bank {
    @Getter
    private UserRepository usersRepository;
    private String bankPassword;
    @Getter
    private String  bankId;
    @Getter
    private Long userId = 0L;
    @Getter
    @Setter
    private BankAdmin bankAdmin;

    public List<User> getAllUsers() {
        return usersRepository.getUsers();
    }

    public void createUser(
            String name,
            String lastName,
            Optional<Long> pasportSerie,
            Optional<String> email) {
        usersRepository.createUser(userId, name, lastName, pasportSerie, email);
    }

    public void addBankAccount(Long id, AccountType accountType) {
        if(usersRepository.getIdForNewAccount(id) == -1) {
            log.info("Для создания аккаунта, надо зарегестрироваться");
            return;
        }
        switch (accountType) {
            case DEBIT:
                usersRepository.addBankAccount(id
                        ,new DebitBankAccount(BigDecimal.ZERO
                            ,usersRepository.getIdForNewAccount(id)
                            ,id
                        , OffsetDateTime.MIN
                        , false));
            case CREDIT:
                usersRepository.addBankAccount(id
                        ,new CreditBankAccount(BigDecimal.ZERO
                                ,usersRepository.getIdForNewAccount(id)
                                ,id
                                , OffsetDateTime.MIN
                        , 10L
                        ,15
                        , 0.005
                        , false));
            case DEPOSIT:
                usersRepository.addBankAccount(id
                        ,new DepositBankAccount(BigDecimal.ZERO
                                ,usersRepository.getIdForNewAccount(id)
                                ,id
                                , OffsetDateTime.MIN
                                , 0L
                                ,15
                        , false));
        }
    }

    public boolean withdrawMoney(long userId, long accountId, long amount, History history) {
        Optional<IBankAccount> res = usersRepository
                .getUserAccounts()
                .get(userId)
                .stream()
                .filter(account -> account.getAccountId() == accountId)
                .findFirst();
        if(res.isEmpty()) {
            return false;
        }
        OperationStatus status = res.get().removeMoney(amount);
        history.addOperation(OperationType.WITHDRAW, status, bankId, userId, accountId, amount, Optional.empty(), Optional.empty(), Optional.empty());
        return switch (status) {
            case SUCCESS -> true;
            case FAIL -> false;
        };
    }

    public void putMoney(long userId, long accountId, long amount, History history) {
        Optional<IBankAccount> res = usersRepository
                .getUserAccounts()
                .get(userId)
                .stream()
                .filter(account -> account.getAccountId() == accountId)
                .findFirst();
        if (res.isEmpty()) {
            log.error("Нет такого аккаунта");
            history.addOperation(OperationType.PUT, OperationStatus.FAIL, bankId, userId, accountId, amount, Optional.empty(), Optional.empty(), Optional.empty());
            return;
        }
        res.get().addMoney(amount);
        history.addOperation(OperationType.PUT, OperationStatus.SUCCESS, bankId, userId, accountId, amount, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public void transferMoney(long fromId, long fromAccountId, long toId, long toAccountId, long amount, History history) {
        Optional<IBankAccount> res = usersRepository
                .getUserAccounts()
                .get(fromId)
                .stream()
                .filter(account -> account.getAccountId() == fromAccountId)
                .findFirst();
        if (res.isEmpty()) {
            log.error("Нет такого аккаунта");
            history.addOperation(OperationType.TRANSFER, OperationStatus.FAIL, bankId, fromId, fromAccountId, amount, Optional.empty(), Optional.of(toId), Optional.of(toAccountId));
            return;
        }
        OperationStatus status = res.get().removeMoney(amount);
        res = usersRepository
                .getUserAccounts()
                .get(toId)
                .stream()
                .filter(account -> account.getAccountId() == toAccountId)
                .findFirst();
        if (res.isEmpty()) {
            log.error("Нет такого маккаунта");
            return;
        }
        res.get().addMoney(amount);
        history.addOperation(OperationType.TRANSFER, status, bankId, fromId, fromAccountId, amount, Optional.empty(), Optional.of(toId), Optional.of(toAccountId));
    }

    public long checkPotentialBalance(long userId, long accountId, Duration time) {
        List<IBankAccount> accounts = usersRepository.getUserAccounts().getOrDefault(userId, List.of());
        if(accounts.isEmpty()) {
            log.error("Неправильный логин юзера или юзера нет аккаунтов");
            return -1L;
        }

        Optional<IBankAccount> bankAccountCheck = accounts.stream().filter(account -> account.getAccountId() == accountId).findFirst();

        if (bankAccountCheck.isEmpty()) {
            log.error("Такого аккаунта нет");
            return -1;
        }

        BigDecimal result = bankAccountCheck.get().checkPotentialBalance(time);
        return result.longValue();
    }

    public long checkCurrentBalance(long userId, long accountId) {
        List<IBankAccount> accounts = usersRepository.getUserAccounts().getOrDefault(userId, List.of());
        if(accounts.isEmpty()) {
            log.error("Неправильный логин юзера или юзера нет аккаунтов");
            return -1L;
        }

        Optional<IBankAccount> bankAccountCheck = accounts.stream().filter(account -> account.getAccountId() == accountId).findFirst();

        if (bankAccountCheck.isEmpty()) {
            log.error("Такого аккаунта нет");
            return -1L;
        }

        BigDecimal result = bankAccountCheck.get().getBalance();
        return result.longValue();
    }
}

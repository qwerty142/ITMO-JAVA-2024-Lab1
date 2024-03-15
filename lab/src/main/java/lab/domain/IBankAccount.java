package lab.domain;

import lombok.NonNull;

import java.math.BigDecimal;
import java.time.Duration;

public interface IBankAccount {
    BigDecimal checkPotentialBalance(Duration time);
    BigDecimal getBalance();
    void addMoney(@NonNull Long amount);
    OperationStatus removeMoney(@NonNull Long amount);
    long getAccountId();

    void setBlockStatus(boolean status);

    AccountType getAccountType();
}

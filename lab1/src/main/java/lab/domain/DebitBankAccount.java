package lab.domain;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
@AllArgsConstructor
@Log4j2
@Setter
public class DebitBankAccount implements IBankAccount {
    private BigDecimal cuurentAmountOfMoney;
    private Long accountId;
    private Long userId;
    private OffsetDateTime timeOfLastIncomeOfMoney;
    private boolean blockStatus = false;
    private double coefficient = 1.15;
    private static final int PARSE_DATE_VALUE = 2592000;
    @Override
    public BigDecimal checkPotentialBalance(Duration time) {
        long diff = (time.getSeconds())/(PARSE_DATE_VALUE);
        return cuurentAmountOfMoney.multiply(BigDecimal.valueOf(coefficient * diff));
    }

    @Override
    public BigDecimal getBalance() {
        return cuurentAmountOfMoney;
    }

    @Override
    public void addMoney(@NonNull @Min(0) Long amount) {
        cuurentAmountOfMoney = cuurentAmountOfMoney.add(BigDecimal.valueOf(amount));
    }

    @Override
    public OperationStatus removeMoney(@NonNull @Min(0) Long amount) {
        if (cuurentAmountOfMoney.longValue() < amount) {
            log.error("Сумма превышает лимит");
            return OperationStatus.FAIL;
        }

        if (blockStatus) {
            log.error("пользователь заблокирован");
            return OperationStatus.FAIL;
        }

        cuurentAmountOfMoney = BigDecimal.valueOf(cuurentAmountOfMoney.doubleValue() - amount.doubleValue());
        return OperationStatus.SUCCESS;
    }

    @Override
    public long getAccountId() {
        return accountId;
    }

    @Override
    public void setBlockStatus(boolean status) {
        blockStatus = status;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.DEBIT;
    }
}

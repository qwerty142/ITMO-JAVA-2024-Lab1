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
public class DepositBankAccount implements IBankAccount {
    private BigDecimal cuurentAmountOfMoney;
    private Long accountId;
    private Long userId;
    private OffsetDateTime timeOfStart;
    private Long timeOfDeposit;
    private double coefficient;
    private boolean blockStatus = false;
    @Override
    public BigDecimal checkPotentialBalance(Duration time) {
        return BigDecimal.valueOf(
                coefficient*cuurentAmountOfMoney.doubleValue()
        );
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

        if (blockStatus) {
            log.error("Пользователь заблокирован");
            return OperationStatus.FAIL;
        }
        long diff = (OffsetDateTime.now().toEpochSecond() - timeOfStart.toEpochSecond());

        if (diff > timeOfDeposit) {
            log.error("Не прошло нужное время с момента пополнения");
            return OperationStatus.FAIL;
        }

        if (cuurentAmountOfMoney.compareTo(BigDecimal.valueOf(amount)) != 1) {
            log.error("На счете недостаточно средств");
            return OperationStatus.FAIL;
        }

        cuurentAmountOfMoney = BigDecimal.valueOf(cuurentAmountOfMoney.longValue() - amount);
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
        return AccountType.DEPOSIT;
    }
}

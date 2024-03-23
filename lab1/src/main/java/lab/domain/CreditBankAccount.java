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
public class CreditBankAccount implements IBankAccount {
    private BigDecimal cuurentAmountOfMoney;
    private Long accountId;
    private Long userId;
    private OffsetDateTime timeOfLastIncomeOfMoney;
    private Long creditLimit;
    private double commission = 1.15;
    private double additionalCoefficient = 0.005;
    private boolean blockStatus = false;

    @Override
    public BigDecimal checkPotentialBalance(Duration time) {
        return cuurentAmountOfMoney
                .multiply(BigDecimal.valueOf(commission + time.getSeconds()*additionalCoefficient/60D/60D));
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
            log.error("Аккаунт заблокирован");
            return OperationStatus.FAIL;
        }
        if (cuurentAmountOfMoney.longValue() > creditLimit) {
            log.error("Превышен лимит");
            return OperationStatus.FAIL;
        }
        cuurentAmountOfMoney = BigDecimal.valueOf(cuurentAmountOfMoney.doubleValue() - amount);
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
        return AccountType.CREDIT;
    }
}

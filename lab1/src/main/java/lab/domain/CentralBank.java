package lab.domain;

import lab.dto.BankAdmin;
import lab.dto.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Objects;
@Log4j2
@AllArgsConstructor
public class CentralBank implements ICentralBank{
    @Getter
    private List<Bank> banks;
    @Getter
    @Setter
    private History history;
    @Override
    public void transfer(String fromBankId, long fromUserId, long accountId, String toBankId, long toUserId, long toAccountId, long amount) {
        Bank fromBank = banks.stream().filter(bank -> Objects.equals(bank.getBankId(), fromBankId)).findFirst().orElse(null);
        if (fromBank == null) {
            log.error("Банк, откуда производится транзакция, не существует");
            return;
        }

        Bank toBank = banks.stream().filter(bank -> Objects.equals(bank.getBankId(), toBankId)).findFirst().orElse(null);
        if (toBank == null) {
            log.error("Банк, куда производится транзакция, не существует");
            return;
        }

        boolean result = fromBank.withdrawMoney(fromUserId, accountId, amount, history);
        if (result) {
            toBank.putMoney(toUserId, toAccountId, amount, history);
        }
    }

    @Override
    public void createBank(String bankId, String bankPassword) {
        if (banks.stream().anyMatch(bank -> Objects.equals(bank.getBankId(), bankId))) {
            log.error("Такой банк уже существует");
            return;
        }
        banks.add(new Bank(new UserRepository(), bankPassword, bankId, 0L, new BankAdmin(bankId, bankPassword, false)));
    }
}

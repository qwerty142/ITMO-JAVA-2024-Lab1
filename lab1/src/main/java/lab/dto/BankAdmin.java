package lab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Log4j2
public class BankAdmin {
    private String bankId;
    private String password;
    @Getter
    private boolean loginStatus = false;
    public boolean register(@NonNull String bankId, @NonNull String password) {
        if (this.bankId.equals(bankId) && this.password.equals(password)) {
            loginStatus = true;
            return true;
        }
        log.error("Неправильный логин или пароль для входа в аккаунт админа банка");
        return false;
    }

    public void banAccount(long userId, long accountId, UserRepository repository) {
        IBankAccount account = repository
                .getUserAccounts().
                get(userId)
                .stream()
                .filter(account -> account.getAccountId() == accountId)
                .findFirst();
        if(account.isEmpty()) {
            log.error("Такого пользователя не существует");
        }

        account.setBlockStatus(true);
    }
}

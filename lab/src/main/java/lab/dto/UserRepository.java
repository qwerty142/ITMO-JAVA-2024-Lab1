package lab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import lab.domain.IBankAccount;

import java.util.*;

@Log4j2
public class UserRepository implements IUsersRepository {
    private Map<Long, User> users;
    @Getter
    private Map<Long, List<IBankAccount>> userAccounts;

    public UserRepository() {
        users = new HashMap<>();
        userAccounts = new HashMap<>();
    }

    @Override
    public void createUser(long id, String name, String lastName, Optional<Long> pasportSerie, Optional<String> email) {
        if (users.containsKey(id)) {
            log.error("This user is already registered");
            return;
        }

        User user = new User(name, lastName, pasportSerie, email, id);
        users.put(id, user);
        userAccounts.put(id, new ArrayList<>());
    }

    @Override
    public void deleteUser(long id) {
        User user = users.getOrDefault(id, null);
        if (user.id() == null) {
            log.error("no such user");
            return;
        }

        users.remove(id);
        userAccounts.remove(id);
    }

    @Override
    public void addBankAccount(Long id, IBankAccount account) {
        List<IBankAccount> list = userAccounts.getOrDefault(id, null);

        if (list == null) {
            log.info("Вы не сможете создать аккаунт, пока не зарегестрируетесь");
            return;
        }

        list.add(account);
    }

    @Override
    public List<User> getUsers() {
        return users.values().stream().toList();
    }

    @Override
    public Long getIdForNewAccount(Long userId) {
        List<IBankAccount> accounts = userAccounts.getOrDefault(userId, null);
        if (accounts == null) {
            log.info("Для создания аккаунта, нужно зарегестрироваться");
            return -1L;
        }

        return (long) accounts.size();
    }

    public User getUser(long id) {
        return users.get(id);
    }
}

package lab.dto;

import lab.domain.IBankAccount;

import java.util.List;
import java.util.Optional;

public interface IUsersRepository {
    void createUser(long id,
                    String name,
                    String lastName,
                    Optional<Long> pasportSerie,
                    Optional<String> email
                    );
    void deleteUser(long id);
    void addBankAccount(Long id, IBankAccount account);

    List<User> getUsers();

    Long getIdForNewAccount(Long userId);
}

package lab.console;

import lab.domain.Bank;
import lab.domain.CentralBank;
import lab.domain.IBankAccount;
import lab.dto.User;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public class State {
    User user;
    long userId;
    Bank bank;
    IBankAccount bankAccount;
    CentralBank centralBank;
}

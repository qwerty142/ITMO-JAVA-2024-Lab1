package lab.console;

import lombok.AllArgsConstructor;
import lab.domain.Bank;
import lab.domain.CentralBank;
import lab.domain.History;
import lab.domain.IBankAccount;
import lab.dto.User;

import java.util.List;

@AllArgsConstructor
public class State {
    User user;
    long userId;
    Bank bank;
    IBankAccount bankAccount;
    CentralBank centralBank = new CentralBank(
            List.of(), new History(List.of())
    );
}

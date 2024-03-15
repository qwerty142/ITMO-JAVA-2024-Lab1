package lab.console;

import lab.domain.AccountType;
import lab.domain.Bank;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Scanner;

public final class ConsoleScenarios {
    private ConsoleScenarios() {}

    public static void selectBankScenario(State state) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Выберите банк, в который вы хотите войти");
        for (int i = 0; i < state.centralBank.getBanks().size(); i++) {
            System.out.println(i + ":" + state.centralBank.getBanks().get(i).getBankId());
        }

        int input = in.nextInt();
        state.bank = state.centralBank.getBanks().get(input);
    }

    public static void createBankScenario(State state) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите уникальный идентификатор");
        String bankId = in.nextLine();
        var ref = new Object() {
            String finalBankId = bankId;
        };
        boolean existStatus = state.centralBank.getBanks().stream().anyMatch(bank -> bank.getBankId() == ref.finalBankId);
        while(existStatus) {
            System.out.println("Такой идентификатор уже есть");
            System.out.println("Введите новый");
            ref.finalBankId = in.nextLine();
            existStatus = state.centralBank.getBanks().stream().anyMatch(bank -> bank.getBankId() == ref.finalBankId);
        }
        System.out.println("Придумайте пароль");
        String password = in.nextLine();
        System.out.println("Придумайте пароль для админа банка");

        state.centralBank.createBank(ref.finalBankId, password);
        state.bank = state
                .centralBank
                .getBanks()
                .stream()
                .filter(bank -> bank.getBankId() == ref.finalBankId)
                .toList()
                .get(0);

    }

    public static void createUserScenario(State state) {
        Scanner in = new Scanner(System.in);
        if(state.bank == null) {
            System.out.println("Прежде, выберите банк, где вы хоитите зарегестрироваться");
            return;
        }

        System.out.println("Напишите поэтапно свое Имя, Фамилию, Серию паспорта и вашу почту");
        String name = in.nextLine();
        String secondName = in.nextLine();
        Long pasport = in.nextLong();
        String email = in.nextLine();
        state.bank.createUser(name, secondName, Optional.of(pasport), Optional.of(email));
        System.out.println("Вы зарегестрированы, ваш уникальный идентификатор: " + state.bank.getUserId());
    }

    public static void loginScenario(State state) {
        Scanner in = new Scanner(System.in);
        if(state.bank == null) {
            System.out.println("Прежде, выберите банк, где вы хоитите зарегестрироваться");
            return;
        }

        System.out.println("Введите ваш уникальный номер");
        long id = in.nextLong();
        state.userId = id;
    }

    public static void putMoneyOnAccountScenario(State state) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите сумму, которую вы желаете положить на счет");
        long money = in.nextLong();

        System.out.println("Введите id аккаунта, на который вы хотите положить деньги");
        long accountId = in.nextLong();

        state.bank.putMoney(state.userId, accountId, money, state.centralBank.getHistory());
    }

    public static void getMoneyFromAccountScenario(State state) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите сумму, которую хотите снять");
        long money = in.nextLong();

        System.out.println("Введите id аккаунта, с которого хотите снять деньги");
        long accountId = in.nextLong();

        state.bank.withdrawMoney(state.userId, accountId, money, state.centralBank.getHistory());
    }

    public static void transferMoneyScenario(State state) {
        Scanner in = new Scanner(System.in);
        System.out.println("Напишите 1, если хотите перевести на аккаунт этого же банка, иначе 0");
        int type = in.nextInt();
        System.out.println("Введите сумму, которую хотите перевести");
        long money = in.nextLong();

        System.out.println("Введите id аккаунта, с которого хотите перевести деньги");
        long accountId = in.nextLong();

        System.out.println("Введите id пользователя, которому вы хотите перевести");
        long toId = in.nextLong();

        if (type == 1) {
            System.out.println("Введите id аккаунта, на который вы хотите перевести");
            long toAccountId = in.nextLong();
            state.bank.transferMoney(state.userId, accountId, toId, toAccountId, money, state.centralBank.getHistory());
        }
        else {
            System.out.println("Введите id банка, в который вы хотите перевести");
            String bankId = in.nextLine();

            System.out.println("Введите id аккаунта, на который вы хотите перевести");
            long toAccountId = in.nextLong();
            state.centralBank.transfer(state.bank.getBankId(), state.userId, accountId, bankId, toId, toAccountId, money);
        }
    }

    public static void banUserAccountScenario(State state) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите id банка");
        String bankId = in.nextLine();

        System.out.println("Введите пароль для входа в аккаунт админа");
        String  password = in.nextLine();

        Optional<Bank> bankCheck = state
                .centralBank
                .getBanks()
                .stream()
                .filter(bank1 -> bank1.getBankId() == bankId)
                .findFirst();
        if (bankCheck.isEmpty()) {
            System.out.println("Нет такого банка");
            return;
        }
        Bank bank = bankCheck.get();
        bank.getBankAdmin().register(bankId, password);
        if (!bank.getBankAdmin().isLoginStatus()) {
            System.out.println("Неверный пароль");
            return;
        }

        System.out.println("Введите id пользователя");
        long id = in.nextLong();

        System.out.println("Введите id аккаунта пользователя");
        long accountId = in.nextLong();
        bank.getBankAdmin().banAccount(id, accountId, bank.getUsersRepository());
    }

    public static void baseOperationsWithAccountScenario(State state) {
        Scanner in = new Scanner(System.in);
        System.out.println("Выберите тип операции");
        System.out.println("1:" + " Проверить потенциальный баланс");
        System.out.println("2:" + " Проверить текущий баланс");

        String ans = in.nextLine();

        switch (ans) {
            case "1":
                System.out.println("Введите промежуток времени в секундах");
                long time = in.nextLong();
                System.out.println("Введите id аккаунта");
                long accountId = in.nextLong();
                long res = state.bank.checkPotentialBalance(state.userId, accountId, Duration.of(time, ChronoUnit.SECONDS));
                if (res != -1) {
                    System.out.println(res);
                }
            case "2":
                System.out.println("Введите id аккаунта");
                long acId = in.nextLong();
                long balance = state.bank.checkCurrentBalance(state.userId, acId);
                if (balance != -1) {
                    System.out.println(balance);
                }
        }
    }

    public static void createBankAccountScenario(State state) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите тип аккаунта который вы хотите создать");
        System.out.println("DEBIT или CREDIT или DEPOSIT");
        String input = in.nextLine();
        AccountType accountType = AccountType.valueOf(input);

        state.bank.addBankAccount(state.userId, accountType);
    }

}

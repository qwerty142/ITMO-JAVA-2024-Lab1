package lab.console;

import java.io.IOException;

public class CLI {
    static State state = new State(null, -1, null, null, null);

    public static void main(String[] args) throws IOException {
        String input = "";
        System.out.println("Вы можете выбрать операции'\n'" +
                "select bank'\n'" +
                "create bank'\n'" +
                "create user'\n'" +
                "login'\n'" +
                "put money'\n'" +
                "get money'\n'" +
                "transfer money'\n'" +
                "ban user account'\n'" +
                "base operations'\n'" +
                "create bank account'\n'" +
                "set commission in bank account'\n'" +
                "set coefficient in bank account'\n'");
        while(input != "escape") {
            parseInput(input);
        }
    }

    public static void parseInput(String input) throws IOException {
        switch (input) {
            case "select bank" -> ConsoleScenarios.selectBankScenario(state);
            case "create bank" -> ConsoleScenarios.createBankScenario(state);
            case "create user" -> ConsoleScenarios.createUserScenario(state);
            case "login" -> ConsoleScenarios.loginScenario(state);
            case "put money" -> ConsoleScenarios.putMoneyOnAccountScenario(state);
            case "get money" -> ConsoleScenarios.getMoneyFromAccountScenario(state);
            case "transfer money" -> ConsoleScenarios.transferMoneyScenario(state);
            case "ban user account" -> ConsoleScenarios.banUserAccountScenario(state);
            case "base operations" -> ConsoleScenarios.baseOperationsWithAccountScenario(state);
            case "create bank account" -> ConsoleScenarios.createBankAccountScenario(state);
            case "set commission in bank account" -> ConsoleScenarios.setCommissionScenario(state);
            case "set coefficient in bank account" -> ConsoleScenarios.setCoefficientScenario(state);
            default -> System.out.println("Нет такой операции");
        }
    }
}

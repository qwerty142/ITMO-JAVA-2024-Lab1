package lab.domain;

public interface ICentralBank {
    void transfer(String fromBankId, long fromUserId, long accountId, String toBankId, long toUserId, long toAccountId, long amount);

    void createBank(String bankId, String bankPassword);
}

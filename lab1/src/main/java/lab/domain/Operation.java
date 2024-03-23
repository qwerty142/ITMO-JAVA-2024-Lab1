package lab.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class Operation {
    private OperationType operationType;
    private OperationStatus operationStatus;
    private String fromBankId;
    private long fromUserId;
    private long fromAccountId;
    private long amount;
    private Optional<String> toBankId;
    private Optional<Long> toUserId;
    private Optional<Long> toAccountId;
}

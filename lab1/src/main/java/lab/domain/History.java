package lab.domain;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
public class History {
    private List<Operation> operations;

    public void addOperation(
            OperationType operationType,
            OperationStatus operationStatus,
            String fromBankId,
            long fromUserId,
            long fromAccountId,
            long amount,
            Optional<String> toBankId,
            Optional<Long> toUserId,
            Optional<Long> toAccountId) {
        operations.add(new Operation(
                operationType,
                operationStatus,
                fromBankId,
                fromUserId,
                fromAccountId,
                amount,
                toBankId,
                toUserId,
                toAccountId
        ));
    }
}

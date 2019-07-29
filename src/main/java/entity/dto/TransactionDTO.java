package entity.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransactionDTO {
    String transactionMessage;
    int crFrom;
    int crTo;
    Double amount;
}

package entity.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransactionDTO {
    String transactionMessage;
    Long crFrom;
    Long crTo;
    Double amount;
}

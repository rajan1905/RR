package transactions;

import entity.dto.TransactionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TransactionRules {

    private static final List<Function<TransactionDTO, Boolean>> RULES = new ArrayList<>();

    private static final Function<TransactionDTO, Boolean> SENDING_TO_OWN_ACCOUNT = transactionDTO ->
            transactionDTO.getCrFrom() == transactionDTO.getCrTo() ? false : true;

    private static final Function<TransactionDTO, Boolean> IS_AMOUNT_NOT_ZERO = transactionDTO ->
            transactionDTO.getAmount() == 0.0 ? false : true;

    public static final Function<TransactionDTO, Boolean> CHECK_TRANSACTION_VALID = transactionDTO ->
        applyRules(transactionDTO);

    private static Boolean applyRules(TransactionDTO transactionDTO){
        Boolean isTransactionValid = true;
        if(RULES.isEmpty()) initRules();

        for(Function<TransactionDTO, Boolean> rule : RULES){
            isTransactionValid &= rule.apply(transactionDTO);
        }

        return isTransactionValid;
    }

    private static void initRules(){
        RULES.add(SENDING_TO_OWN_ACCOUNT);
        RULES.add(IS_AMOUNT_NOT_ZERO);
    }
}

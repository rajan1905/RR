package entity.dao;

import javax.persistence.*;

@Table(name = "Transactions")
@Entity
public class Transactions {

    @Id
    @Column(name="transactionNo",nullable=false)
    int transactionNo;

    @Column(name="TRNX_MSG",nullable=false)
    String transactionMessage;

    @Column(name="CR_FROM")
    int crFrom;

    @Column(name="CR_TO")
    int crTo;

    @Column(name="amount")
    Double amount;

}

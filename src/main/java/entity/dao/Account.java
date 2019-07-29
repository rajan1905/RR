package entity.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "Account")
@Entity
@Getter
@AllArgsConstructor
public class Account implements Serializable {

    @Column(name="FNAME",length=100,nullable=false)
    String firstName;

    @Column(name="MNAME",length=100)
    String middleName;

    @Column(name="LNAME",length=100,nullable=false)
    String lastName;

    @Id
    @Column(name="ACCOUNTNO",nullable=false)
    int accountNo;

    @Column(name="BALANCE",nullable=false)
    Double balance;
}

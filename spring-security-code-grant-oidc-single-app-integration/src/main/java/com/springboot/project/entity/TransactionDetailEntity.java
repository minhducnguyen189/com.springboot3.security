package com.springboot.project.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction_detail")
public class TransactionDetailEntity extends BaseEntity {

    @Serial private static final long serialVersionUID = 9165902569499521742L;

    @Generated
    @Column(name = "transaction_number")
    private String transactionNumber;

    @Column(name = "sequence_number")
    private Long sequenceNumber;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "domain")
    private TransactionDomainEnumEntity domain;

    @Column(name = "location")
    private String location;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private TransactionStatusEnumEntity status;

    @Column(name = "payment_method")
    @Enumerated(value = EnumType.STRING)
    private PaymentMethodEnumEntity paymentMethod;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "net_value")
    private BigDecimal netValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    private BankAccountEntity bankAccount;
}

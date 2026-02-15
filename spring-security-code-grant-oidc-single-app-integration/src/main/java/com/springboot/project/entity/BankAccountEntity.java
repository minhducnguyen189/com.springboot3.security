package com.springboot.project.entity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Generated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bank_account")
public class BankAccountEntity extends BaseEntity {

    @Serial private static final long serialVersionUID = -7913273199791663507L;

    @Generated
    @Column(name = "sequence_number")
    private Long sequenceNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "street")
    private String street;

    @Column(name = "street_number")
    private String streetNumber;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_type")
    @Enumerated(value = EnumType.STRING)
    private AccountTypeEnumEntity accountType;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "currency")
    @Enumerated(value = EnumType.STRING)
    private CurrencyEnumEntity currency;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private AccountStatusEnumEntity status;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "bankAccount")
    private List<TransactionDetailEntity> transactions;
}

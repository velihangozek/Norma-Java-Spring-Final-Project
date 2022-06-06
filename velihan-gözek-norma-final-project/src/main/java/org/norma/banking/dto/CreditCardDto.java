package org.norma.banking.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.norma.banking.entity.Expenses;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardDto {

    private int id;

    private String cardNo;

    private String cardType;

    private double amount;

    private String cardPassword;

    private double cardLimit;

    private List<Expenses> expenses;

    private double cardDebt;

    private LocalDate expiredDate;

    private int ccv;

    @CreationTimestamp
    private LocalDate createdDate;

    private List<AccountDto> accounts;

}

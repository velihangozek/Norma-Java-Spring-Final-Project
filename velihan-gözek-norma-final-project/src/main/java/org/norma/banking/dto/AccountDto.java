package org.norma.banking.dto;


import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private int id;

    private double balance;

    private String currency;

    private String accountType;

    private String iban;

    private LocalDate createdDate;

}

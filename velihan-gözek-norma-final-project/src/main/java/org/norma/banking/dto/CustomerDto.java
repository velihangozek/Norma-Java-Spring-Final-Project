package org.norma.banking.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.norma.banking.entity.Address;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private int id;

    private String fullName;

    private String identityNumber;

    private String password;

    private String description;

    private String email;

    private String phoneNumber;

    @CreationTimestamp
    private LocalDate createdDate;

    private Address address;

    private List<AccountDto> accounts;

    private List<CreditCardDto> creditCards;
}

package org.norma.banking.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private double amount;

    private String senderIban;

    private String receiverIban;

    private String currency;

    private String accountType;

    @CreationTimestamp
    private LocalDate date;

    @ManyToMany(mappedBy = "transfers", cascade = CascadeType.ALL)
    private List<Account> accounts;

}

package org.norma.banking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String cardNo;

    private double amount;

    private String cardType;

    private String cardPassword;

    private double cardLimit;

    private double cardDebt;

    @CreationTimestamp
    private LocalDate createdDate;

    private LocalDate expiredDate = LocalDate.of(2022,8,21);

    private int ccv;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private List<Expenses> expenses;

    @ManyToMany
    private List<Account> accounts;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonIgnore
    private Customer customer;


    @Override
    public String toString() {
        return "";
    }
}


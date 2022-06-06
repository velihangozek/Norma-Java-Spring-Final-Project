package org.norma.banking.repository;

import org.norma.banking.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CreditCardRepository extends CrudRepository<Card, Integer> {

    Page<Card> findAll(Pageable page);

    Card getById(Integer id);

}

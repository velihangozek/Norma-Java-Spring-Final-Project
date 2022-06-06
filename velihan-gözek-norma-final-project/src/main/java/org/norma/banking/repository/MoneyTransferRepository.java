package org.norma.banking.repository;

import org.norma.banking.entity.Transfer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MoneyTransferRepository extends CrudRepository<Transfer, Integer> {

    List<Transfer> findAll();

}

package org.norma.banking.controller;

import lombok.RequiredArgsConstructor;
import org.norma.banking.dto.MoneyTransferDto;
import org.norma.banking.external.ExchangeRates;
import org.norma.banking.entity.Transfer;
import org.norma.banking.service.TransferService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/transfer")
public class MoneyTransferController {


    private final TransferService transferService;

    @GetMapping(value = "/exchangerates", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ExchangeRates getExchangeList(){
        return transferService.exchangeRate();
    }


    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Transfer> getTransferDetails(){
        try{
            return transferService.findAll();
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transaction found!");
        }
    }

    @PostMapping(value = "/iban")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Transfer transferMoney(@RequestBody MoneyTransferDto moneyTransferDto, @Param(value = "sender") String iban1, @Param(value = "receiver") String iban2){
        try {
            return transferService.sendMoney(moneyTransferDto, iban1, iban2);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Money transferring process could not be completed from "+iban1+" to "+iban2);
        }
    }

}

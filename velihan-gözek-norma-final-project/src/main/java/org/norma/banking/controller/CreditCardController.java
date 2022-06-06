package org.norma.banking.controller;

import org.norma.banking.dto.CreditCardDto;
import org.norma.banking.entity.Expenses;
import org.norma.banking.service.CreditCardService;
import org.norma.banking.entity.Card;
import org.norma.banking.transformer.CreditCardTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Controller
@RestController
@RequestMapping("/v1/api/card")
public class CreditCardController {

    private final CreditCardService creditCardService;

    private final CreditCardTransformer creditCardTransformer;

    public CreditCardController(CreditCardService creditCardService, CreditCardTransformer creditCardTransformer) {
        this.creditCardService = creditCardService;
        this.creditCardTransformer = creditCardTransformer;
    }

    @PutMapping(value = "/custno/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCard(@RequestBody CreditCardDto creditCardDto, @PathVariable int id){
        try {
            creditCardService.addCard(id, creditCardDto);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit Card could not be created for id"+id);
        }
    }

    @GetMapping(params = {"page","size"})
    @ResponseStatus(value = HttpStatus.OK)
    public List<CreditCardDto> listCard(@RequestParam("page") int page, @RequestParam("size") int size){
        try {
            return creditCardService.list(PageRequest.of(page,size)).stream()
                    .map(creditCardTransformer::toCardDto)
                    .collect(Collectors.toList());
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no card in database");
        }

    }

    @GetMapping(value = "/debtamount/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public HashMap<String,Double> cardDebt(@PathVariable int id){
        try {
            return creditCardService.findAllCardDebt(id);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Debt is not found with id"+id);
        }

    }

    @PutMapping(value = "/shop/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Card shoppingWithCard(@RequestBody CreditCardDto creditCardDto, @PathVariable int id){
        try {
            return creditCardService.transferMoneyForShopping(id, creditCardDto);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not complete shopping for the id"+id);
        }

    }

    @PutMapping(value = "/creditcarddebtpay/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Card payCardDebt(@RequestBody CreditCardDto creditCardDto, @PathVariable int id){
        try {
            return creditCardService.debtPaymentFromAccount(id, creditCardDto);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit card debt pay process could not be completed for the id"+id);
        }

    }

    @PutMapping(value = "/cashpoint/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Card PayCardDebtFromCashpoint(@RequestBody CreditCardDto creditCardDto, @PathVariable int id){
        try {
            return creditCardService.debtPaymentFromCashpoint(id, creditCardDto);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit card debt from cashpoint process could not be completed for the id"+id);
        }

    }

    @GetMapping(value = "/creditcardreceipt/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Expenses> creditCardReceipt(@PathVariable int id){
        try {
            return creditCardService.listReceipt(id);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card receipt info not found within id"+id);
        }

    }

}

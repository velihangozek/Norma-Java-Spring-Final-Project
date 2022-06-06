package org.norma.banking.controller;

import lombok.RequiredArgsConstructor;

import org.norma.banking.service.CustomerService;
import org.norma.banking.dto.CustomerDto;
import org.norma.banking.entity.Customer;
import org.norma.banking.transformer.CustomerTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/customer")
public class CustomerController {

    private final CustomerTransformer customerTransformer;

    private final CustomerService customerService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto create(@RequestBody CustomerDto customerDto){
        try {
            return customerService.createCustomer(customerDto);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer could not be created!");
        }

    }

    @GetMapping(params = {"page","size"})
    public List<CustomerDto> list(@RequestParam("page") int page, @RequestParam("size") int size){
        try {
            return customerService.listAll(PageRequest.of(page, size)).stream()
                    .map(customerTransformer::toCustomerDto)
                    .collect(Collectors.toList());
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer found in database");
        }
    }

    @GetMapping(value = "/{id}")
    public Customer getCustomerById(@PathVariable int id){
        try {
            return customerService.getById(id);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id"+id);
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto updateCustomerById(@PathVariable int id, @RequestBody CustomerDto customerDto){
        try {
            return customerService.updateCustomer(customerDto, id);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer could not be updated with id "+id);
        }
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerById(@PathVariable int id){
        try {
            customerService.deleteCustomer(id);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer could not be deleted with id"+id);
        }
    }

}

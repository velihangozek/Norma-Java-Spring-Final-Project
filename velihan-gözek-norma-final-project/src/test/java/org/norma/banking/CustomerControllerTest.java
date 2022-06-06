package org.norma.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.norma.banking.dto.AccountDto;
import org.norma.banking.dto.CreditCardDto;
import org.norma.banking.dto.CustomerDto;
import org.norma.banking.entity.Customer;
import org.norma.banking.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

    @SpringBootTest
    @AutoConfigureMockMvc
    public class CustomerControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private CustomerRepository customerRepository;


        @Test
        public void testCreateCustomer() throws Exception {

            String url = "/v1/api/customer";
            CustomerDto customerDto = new CustomerDto();

            customerDto.setIdentityNumber("customerDto.getTC()");
            customerDto.setFullName("ADASDSA");
            customerDto.setEmail("customerDto.getEmail()");
            customerDto.setPhoneNumber("customerDto.getPhone()");
            customerDto.setPassword(customerDto.getPassword());
            List<AccountDto> accounts = new ArrayList<>();
            AccountDto accountDto = new AccountDto();
            accountDto.setAccountType("CHECKING");
            accountDto.setCurrency("TRY");
            accounts.add(accountDto);
            customerDto.setAccounts(accounts);
            List<CreditCardDto> cards = new ArrayList<>();
            CreditCardDto creditCardDto = new CreditCardDto();
            creditCardDto.setCardPassword("asdasdsa");
            creditCardDto.setCardLimit(1000);
            creditCardDto.setCardType("CREDIT");
            cards.add(creditCardDto);
            customerDto.setCreditCards(cards);

            mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(customerDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        public void testUpdateCustomer() throws Exception {

            String url = "/v1/api/customer/{id}";
            int customerId = 1;

            Customer customer = new Customer();

            customer.setId(customerId);
            customer.setFullName("customerDto.getFullName()");
            customer.setPassword("customerDto.getPassword()");
            customer.setDescription("customerDto.getDescription()");
            customer.setEmail("customerDto.getEmail()");
            customer.setIdentityNumber("customerDto.getTC()");
            customer.setPhoneNumber("customerDto.getPhone()");

            CustomerDto customerDto = new CustomerDto();

            customerDto.setFullName("customerDto.getFullName()");
            customerDto.setPassword("customerDtogetPassword");
            customerDto.setDescription("customerDto.getDescription()");
            customerDto.setEmail("customerDto.getEmail()");
            customerDto.setIdentityNumber("customerDto.getTC()");
            customerDto.setPhoneNumber("customerDto.getPhone()");

            customerRepository.save(customer);

            mockMvc.perform(put(url,customerId).contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(customerDto)))
                    .andExpect(status().isOk());
        }

        @Test
        public void testList() throws Exception{
            mockMvc.perform(get("/v1/api/customer?page=0&size=20").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        public void testCustomerById() throws Exception {
            mockMvc.perform(get("/v1/api/customer/{id}",1)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        public void testDeleteCustomer() throws Exception {
            mockMvc.perform(delete("/v1/api/customer/delete/{id}",1)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }



    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    }


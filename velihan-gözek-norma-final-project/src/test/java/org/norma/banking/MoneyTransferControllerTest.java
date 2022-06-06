package org.norma.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.norma.banking.entity.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MoneyTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String url = "/v1/api/transfer";

    @Test
    public void testGetTransferDetails() throws Exception {

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void testSendMoney() throws Exception {
        Transfer transfer = new Transfer();

        String iban1 = "iban1";
        String iban2 = "iban2";

        transfer.setAmount(100);
        transfer.setSenderIban(iban1);
        transfer.setReceiverIban(iban2);
        transfer.setCurrency("TRY");
        transfer.setAccountType("CHECKING");

        mockMvc.perform(post(url+"/iban").contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(transfer))).andExpect(status().isBadRequest());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

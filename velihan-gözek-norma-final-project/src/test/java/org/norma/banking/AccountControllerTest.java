package org.norma.banking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String url = "/v1/api/account";


    @Test
    public void testList() throws Exception {
        mockMvc.perform(get(url+"/?page=0&size=20").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteAccount() throws Exception {
        mockMvc.perform(delete(url+"/delete/{id}",1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindAccountById() throws Exception{
        mockMvc.perform(get(url+"/{id}",1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}

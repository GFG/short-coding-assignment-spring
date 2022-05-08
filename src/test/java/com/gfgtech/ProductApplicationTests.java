package com.gfgtech;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfgtech.email.EmailSender;
import com.gfgtech.product.model.Product;
import com.gfgtech.product.web.ProductController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//todo: add tests for checking email message
class ProductApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmailSender emailSender;

    @Test
    void contextLoads() {
    }

    @Test
    void testPriceUpdatedAndEmailSent() throws Exception {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(150));

        mockMvc.perform(patch("/products/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(jsonPath("$.price").value("150"))
                .andExpect(status().is2xxSuccessful());

        Set<String> emails = Set.of(ProductController.EMAIL_SELLER, ProductController.EMAIL_ADMIN);
        verify(emailSender, times(1)).sendMail(any(), argThat(r -> r.containsAll(emails) && r.size() == emails.size()));
    }

    @Test
    void testNonPriceFieldUpdatedAndEmailSent() throws Exception {
        Product product = new Product();
        product.setColor("newcolor");

        mockMvc.perform(patch("/products/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(jsonPath("$.color").value("newcolor"))
                .andExpect(status().is2xxSuccessful());

        Set<String> emails = Set.of(ProductController.EMAIL_SELLER);
        verify(emailSender, times(1)).sendMail(any(), argThat(r -> r.containsAll(emails) && r.size() == emails.size()));
    }

    @Test
    void testNoEmailIfNoChanges() throws Exception {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(100));

        mockMvc.perform(patch("/products/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().is2xxSuccessful());

        verify(emailSender, times(0)).sendMail(any(), any());
    }

    @Test
    void testUpdateWithNotValidPrice() throws Exception {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-1));

        mockMvc.perform(patch("/products/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateWithTooBigPriceDiff() throws Exception {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(20));
        mockMvc.perform(patch("/products/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateNotExistingProduct() throws Exception {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(100));

        mockMvc.perform(patch("/products/-1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());
    }
}

package com.ajopaul.qantas.customerprofile;

import com.mscharhag.oleaster.runner.OleasterRunner;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.Optional;

import static com.ajopaul.qantas.customerprofile.CustomerNotFound.CUSTOMER_NOT_FOUND;
import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.before;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;
import static java.util.Arrays.asList;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(OleasterRunner.class)
public class CustomerProfileControllerTest {

    @Mock
    private CustomerProfileService customerProfileService;

    @InjectMocks
    private CustomerProfileController customerProfileController;

    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    {
        before(() -> {
            MockitoAnnotations.initMocks(this);
            this.mockMvc = MockMvcBuilders.standaloneSetup(customerProfileController).build();
            RestAssuredMockMvc.standaloneSetup(customerProfileController);
        });

        describe("/api/customers", () -> {
            it("Should return all the customers", () -> {
                when(customerProfileService.getAllCustomers()).thenReturn(asList(getBaseCustomer(), Customer.builder()
                    .firstName("Test2")
                    .lastName("Woo")
                    .dateOfBirth("10-10-1999")
                    .homeAddress("03030303")
                    .officeAddress("05050505")
                    .email("xyz@email")
                    .build()));

                this.mockMvc.perform(get("/api/customers"))
                    .andExpect(status().isOk())
                        .andExpect(content().contentType(contentType))
                        .andExpect(content().json("{\n" +
                            "    \"data\": [{\n" +
                            "        \"firstName\": \"Test\",\n" +
                            "        \"lastName\": \"John\",\n" +
                            "        \"dateOfBirth\": \"11-11-2000\",\n" +
                            "        \"homeAddress\": \"02020202\",\n" +
                            "        \"officeAddress\": \"04040404\",\n" +
                            "        \"email\": \"abc@email\"\n" +
                            "    },{\n" +
                            "            \"firstName\": \"Test2\",\n" +
                            "            \"lastName\": \"Woo\",\n" +
                            "            \"dateOfBirth\": \"10-10-1999\",\n" +
                            "            \"homeAddress\": \"03030303\",\n" +
                            "            \"officeAddress\": \"05050505\",\n" +
                            "            \"email\": \"xyz@email\"\n" +
                            "    }]\n" +
                            "}"));

                Mockito.verify(customerProfileService, times(1)).getAllCustomers();
            });
        });

        describe("GET /api/customers/{id}", () -> {
           it("Should return an existing customer", () -> {
               long customerId = 111L;
               Customer customer = getBaseCustomer();

               when(customerProfileService.getCustomerProfile(customerId)).thenReturn(customer);

               this.mockMvc.perform(get("/api/customers/"+customerId))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(contentType))
                   .andExpect(content().json("{\n" +
                           "    \"data\": {\n" +
                           "      \"firstName\": \"Test\",\n" +
                           "      \"lastName\": \"John\",\n" +
                           "      \"dateOfBirth\": \"11-11-2000\",\n" +
                           "      \"homeAddress\": \"02020202\",\n" +
                           "      \"officeAddress\": \"04040404\",\n" +
                           "      \"email\": \"abc@email\"\n" +
                           "    }\n" +
                           "}"));

           });

           it("Should return 404, when customer does not exist", () -> {
               long customerId = -1L;
               when(customerProfileService.getCustomerProfile(customerId)).
                       thenThrow(CustomerNotFound.builder()
                               .id(customerId)
                               .shortMessage(CUSTOMER_NOT_FOUND)
                               .build());

               this.mockMvc.perform(get("/api/customers/"+customerId)).andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));

               this.mockMvc.perform(get("/api/customers/"+customerId))
                       .andExpect(status().isNotFound())
                       .andExpect(content().contentType(contentType))
                       .andExpect(content().json("{\n" +
                               "    \"data\": null,\n" +
                               "    \"error\": {\n" +
                               "        \"title\": \"Customer Not Found\",\n" +
                               "        \"detail\": \"Customer not found for id: -1\",\n" +
                               "        \"status\": \"404 NOT_FOUND\"\n" +
                               "    }\n" +
                               "}"));
           });
        });

        describe("DELETE /api/customers/{id}", () -> {
           it("should delete a customer if it exists", () -> {

           });

           it("should return 404, when customer does not exist", () -> {
               fail();
           });
        });
    }

    private Customer getBaseCustomer() {
        return Customer.builder()
            .firstName("Test")
            .lastName("John")
            .dateOfBirth("11-11-2000")
            .homeAddress("02020202")
            .officeAddress("04040404")
            .email("abc@email")
            .build();
    }
}

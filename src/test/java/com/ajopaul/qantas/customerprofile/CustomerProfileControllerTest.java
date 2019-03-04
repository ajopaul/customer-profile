package com.ajopaul.qantas.customerprofile;

import com.mscharhag.oleaster.runner.OleasterRunner;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.mscharhag.oleaster.runner.StaticRunnerSupport.before;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;
import static java.util.Arrays.asList;
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
                    .andExpect(content().json("[{\n" +
                        "  \"firstName\": \"Test\",\n" +
                        "  \"lastName\": \"John\",\n" +
                        "  \"dateOfBirth\": \"11-11-2000\",\n" +
                        "  \"homeAddress\": \"02020202\",\n" +
                        "  \"officeAddress\": \"04040404\",\n" +
                        "  \"email\": \"abc@email\"\n" +
                        "},\n" +
                        "  {\n" +
                        "    \"firstName\": \"Test2\",\n" +
                        "    \"lastName\": \"Woo\",\n" +
                        "    \"dateOfBirth\": \"10-10-1999\",\n" +
                        "    \"homeAddress\": \"03030303\",\n" +
                        "    \"officeAddress\": \"05050505\",\n" +
                        "    \"email\": \"xyz@email\"\n" +
                        "  }]"));

                Mockito.verify(customerProfileService, times(1)).getAllCustomers();
            });
        });

        describe("/api/customers{id}", () -> {
           it("Should return an existing customer", () -> {
               long customerId = 111L;
               Customer customer = getBaseCustomer();

               when(customerProfileService.getCustomerProfile(customerId)).thenReturn(customer);

               this.mockMvc.perform(get("/api/customers/"+customerId))
                   .andExpect(status().isOk())
                   .andExpect(content().json("{\n" +
                       "  \"firstName\": \"Test\",\n" +
                       "  \"lastName\": \"John\",\n" +
                       "  \"dateOfBirth\": \"11-11-2000\",\n" +
                       "  \"homeAddress\": \"02020202\",\n" +
                       "  \"officeAddress\": \"04040404\",\n" +
                       "  \"email\": \"abc@email\"\n" +
                       "}"));

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

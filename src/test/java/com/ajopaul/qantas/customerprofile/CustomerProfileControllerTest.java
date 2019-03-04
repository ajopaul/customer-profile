package com.ajopaul.qantas.customerprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

public class CustomerProfileControllerTest {

    @Autowired
    private CustomerProfileController customerProfileController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
}

package com.ajopaul.qantas.customerprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerProfileService {


    @Autowired
    private CustomerProfileDataService customerProfileDataService;

    public List<Customer> getAllCustomers() {
        return customerProfileDataService.findAll().stream().map(customerProfileData -> Customer.builder()
                .firstName(customerProfileData.getFirstName())
                .lastName(customerProfileData.getLastName())
                .build()).collect(Collectors.toList());
    }
}

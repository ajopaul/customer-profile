package com.ajopaul.qantas.customerprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerProfileController {

/*
    @GetMapping("/customers")
    public List<CustomerProfileData> fetchAllCustomers() {
        return customerProfileDataService.findAll();
    }

    @GetMapping("/customers/{id}")
    public CustomerProfileData fetchCustomerProfile(@PathVariable long id) {
        return customerProfileDataService
                .findById(id)
                .orElseThrow((() -> new RuntimeException("Not Found")));
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable long id) {
        CustomerProfileData customerProfileData = customerProfileDataService.findById(id).orElseThrow(() -> CustomerNotFound.builder().id(id).build());

        customerProfileDataService.delete(customerProfileData);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/customers")
    public CustomerProfileData createCustomer(@RequestBody CustomerProfileData customerProfileData) {

        return customerProfileDataService.save(customerProfileData);

    }

    @PutMapping("/customers/{id}")
    public CustomerProfileData updateCustomer(@RequestBody CustomerProfileData customer, @PathVariable long id) {

        CustomerProfileData customerProfileData = customerProfileDataService.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));

        customerProfileData.setFirstName(customer.getFirstName());
        customerProfileData.setLastName(customer.getLastName());
        customerProfileData.setDateOfBirth(customer.getDateOfBirth());
        customerProfileData.setAddress(customer.getAddress());

        return customerProfileDataService.save(customerProfileData);
    }*/
}

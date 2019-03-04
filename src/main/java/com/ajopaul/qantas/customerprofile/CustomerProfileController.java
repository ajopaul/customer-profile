package com.ajopaul.qantas.customerprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerProfileController {

    @Autowired
    private CustomerProfileService customerProfileService;

    @GetMapping(value = "/customers", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> fetchAllCustomers() {
        return ResponseEntity.ok()
                .body(ResponseData.success(customerProfileService.getAllCustomers()));
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<?> fetchCustomerProfile(@PathVariable long id) {
        try {
            Customer  customerProfile = customerProfileService.getCustomerProfile(id);
            return ResponseEntity
                    .ok()
                    .body(ResponseData.success(customerProfile));
        } catch (CustomerNotFound e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseData.error(HttpStatus.NOT_FOUND, e.getShortMessage(), e.getMessage()));
        }
    }

    /*@DeleteMapping("/customers/{id}")
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

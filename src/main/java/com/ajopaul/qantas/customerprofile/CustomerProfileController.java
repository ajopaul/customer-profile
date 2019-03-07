package com.ajopaul.qantas.customerprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;

@RestController
@RequestMapping("/api")
public class CustomerProfileController {

    @Autowired
    private CustomerProfileService customerProfileService;

    private MediaType JSON_CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    @GetMapping(value = "/customers", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> fetchAllCustomers() {
        return ResponseEntity.ok()
                .contentType(JSON_CONTENT_TYPE)
                .body(ResponseData.builder().data(customerProfileService.getAllCustomers()).build());
    }

    @GetMapping(value = "/customers/{id}", produces = "application/json")
    public ResponseEntity<?> fetchCustomerProfile(@PathVariable long id) {
        try {
            Customer  customerProfile = customerProfileService.getCustomerProfile(id);

            return ResponseEntity
                    .ok()
                    .contentType(JSON_CONTENT_TYPE)
                    .body(ResponseData.builder().data(customerProfile).build());
        } catch (CustomerNotFound e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(JSON_CONTENT_TYPE)
                    .body(ResponseData.error(HttpStatus.NOT_FOUND, e.getShortMessage(), e.getMessage()));
        }
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable long id) {
        try {
            customerProfileService.deleteCustomer(id);

            return ResponseEntity.noContent().build();
        } catch (CustomerNotFound e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(JSON_CONTENT_TYPE)
                    .body(ResponseData.error(HttpStatus.NOT_FOUND, e.getShortMessage(), e.getMessage()));
        }
    }

    @ResponseBody
    @RequestMapping(consumes="application/json",
            produces="application/json",
            method=RequestMethod.POST,
            value="/customers")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {

            Customer  customerProfile = customerProfileService.createCustomer(customer);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(JSON_CONTENT_TYPE)
                    .body(ResponseData.builder().data(customerProfile).build());
    }

    @ResponseBody
    @RequestMapping(consumes="application/json",
            produces="application/json",
            method=RequestMethod.PUT,
            value="/customers/{id}")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer, @PathVariable long id) {

        try {

            if (!isRequestValid(customer)) {
                return ResponseEntity
                        .badRequest()
                        .contentType(JSON_CONTENT_TYPE)
                        .body(ResponseData
                                .error(HttpStatus.BAD_REQUEST,
                                        ResponseData.SOMETHING_WENT_WRONG,
                                        "Request body invalid"));
            }
            Customer  customerProfile = customerProfileService.updateCustomer(customer, id);

            return ResponseEntity
                    .ok()
                    .contentType(JSON_CONTENT_TYPE)
                    .body(ResponseData.builder().data(customerProfile).build());
        } catch (CustomerNotFound e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(JSON_CONTENT_TYPE)
                    .body(ResponseData.error(HttpStatus.NOT_FOUND, e.getShortMessage(), e.getMessage()));
        }

    }

    private boolean isRequestValid(Customer customer) {

        return !(customer.getFirstName() == null && customer.getLastName() == null
                && customer.getDateOfBirth() == null && customer.getHomeAddress() == null
                && customer.getOfficeAddress() == null && customer.getEmail() == null);
    }
}

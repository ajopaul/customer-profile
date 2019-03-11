package com.ajopaul.qantas.customerprofile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value = "customer-profile", description = "REST Api to manage customer profile")
public class CustomerProfileController {

    @Autowired
    private CustomerProfileService customerProfileService;

    private MediaType JSON_CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    @ApiOperation(value = "Find all Customers")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
    @GetMapping(value = "/customers", produces = "application/json")
    @ResponseBody
    public ResponseEntity<ResponseData<List<Customer>>> fetchAllCustomers() {

        ResponseData.ResponseDataBuilder < List<Customer> > responseDataBuilder = ResponseData.<List<Customer>>builder()
                .data(customerProfileService.getAllCustomers());

        return ResponseEntity.ok()
                .contentType(JSON_CONTENT_TYPE)
                .body(responseDataBuilder.build());
    }

    @ApiOperation(value = "Find Customer by Id", response = ResponseCustomerData.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Customer Not Found")})
    @GetMapping(value = "/customers/{id}", produces = "application/json")
    public ResponseEntity<ResponseData<?>> fetchCustomerProfile(@PathVariable long id) {
        try {
            Customer  customerProfile = customerProfileService.getCustomerProfile(id);

            return ResponseEntity
                    .ok()
                    .contentType(JSON_CONTENT_TYPE)
                    .body(ResponseData.<Customer>builder().data(customerProfile).build());
        } catch (CustomerNotFound e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(JSON_CONTENT_TYPE)
                    .body(ResponseData.error(HttpStatus.NOT_FOUND, e.getShortMessage(), e.getMessage()));
        }
    }

    @ApiOperation(value = "Delete Customer by Id")
    @ApiResponses(value = {@ApiResponse(code = 204, message = ""), @ApiResponse(code = 404, message = "Customer Not Found")})
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<ResponseData> deleteCustomer(@PathVariable long id) {
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

    @ApiOperation(value = "Create a Customer")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "CREATED")})
    @ResponseBody
    @RequestMapping(consumes="application/json",
            produces="application/json",
            method=RequestMethod.POST,
            value="/customers")
    public ResponseEntity<ResponseData<Customer>> createCustomer(@RequestBody Customer customer) {

            Customer  customerProfile = customerProfileService.createCustomer(customer);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(JSON_CONTENT_TYPE)
                    .body(ResponseData.<Customer>builder().data(customerProfile).build());
    }

    @ApiOperation(value = "Update Customer by Id", response = ResponseCustomerData.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Customer Not Found")})
    @ResponseBody
    @RequestMapping(consumes="application/json",
            produces="application/json",
            method=RequestMethod.PUT,
            value="/customers/{id}")
    public ResponseEntity<ResponseData<?>> updateCustomer(@RequestBody Customer customer, @PathVariable long id) {

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
                    .body(ResponseData.<Customer>builder().data(customerProfile).build());
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

    private class ResponseCustomerData extends ResponseData<Customer> {
        ResponseCustomerData(Customer data, Error error) {
            super(data, error);
        }
    }
}

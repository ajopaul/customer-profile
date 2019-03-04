package com.ajopaul.qantas.customerprofile;

import com.mscharhag.oleaster.runner.OleasterRunner;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.before;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;
import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(OleasterRunner.class)
public class CustomerProfileServiceTest {

    @Mock
    CustomerProfileDataService customerProfileDataService;

    @InjectMocks
    CustomerProfileService customerProfileService;

    {
        before(() -> {
            MockitoAnnotations.initMocks(this);
        });

        describe("get all customers", () -> {
            it("should get all customers", () -> {
                List<CustomerProfileData> customerProfileDataList = asList(getBaseCustomerProfileData(111L),
                    CustomerProfileData.builder()
                        .id(222L)
                        .firstName("Test2")
                        .lastName("Woo")
                        .dateOfBirth("10-10-1999")
                        .homeAddress("03030303")
                        .officeAddress("05050505")
                        .email("xyz@email")
                        .build());
                when(customerProfileDataService.findAll()).thenReturn(customerProfileDataList);

                List<Customer> allCustomers = customerProfileService.getAllCustomers();

                verify(customerProfileDataService, times(1)).findAll();

                expect(allCustomers).toEqual(asList(getBaseCustomer().toBuilder().id(111).build(), Customer.builder()
                        .id(222)
                        .firstName("Test2")
                        .lastName("Woo")
                        .dateOfBirth("10-10-1999")
                        .homeAddress("03030303")
                        .officeAddress("05050505")
                        .email("xyz@email")
                        .build()));
            });
        });

        describe("get a single customer", () -> {

            it("should return valid customer", () -> {
                long customerId = 111L;
                Optional<CustomerProfileData> customerProfileData = Optional.of(getBaseCustomerProfileData(customerId));

                when(customerProfileDataService.findById(customerId)).thenReturn(customerProfileData);

                Customer customer = customerProfileService.getCustomerProfile(customerId);

                verify(customerProfileDataService, times(1)).findById(customerId);

                expect(customer).toEqual(getBaseCustomer().toBuilder().id(111).build());
            });

            it("should throw exception when id is not found", () -> {
                long customerId = 111L;
                when(customerProfileDataService.findById(customerId)).thenReturn(Optional.empty());
                    expect(() -> {
                        customerProfileService.getCustomerProfile(customerId);
                    }).toThrow(CustomerNotFound.class);

            });
        });

        describe("delete a customer", () -> {
            it("Should delete a customer for a given id", () -> {
                long customerId = 111L;
                Optional<CustomerProfileData> customerProfileData = Optional.of(getBaseCustomerProfileData(customerId));

                when(customerProfileDataService.findById(customerId)).thenReturn(customerProfileData);

                customerProfileService.deleteCustomer(customerId);

                verify(customerProfileDataService, times(1)).findById(customerId);
                verify(customerProfileDataService, times(1)).delete(customerProfileData.get());
            });

            it("should throw customer not found for an invalid id", () -> {
                long customerId = 111L;
                when(customerProfileDataService.findById(customerId)).thenReturn(Optional.empty());
                expect(() -> {
                    customerProfileService.deleteCustomer(customerId);
                }).toThrow(CustomerNotFound.class);
            });
        });

        describe("Create a customer", () -> {
            it("should create a customer from request", () -> {
               CustomerProfileData customerProfileData = getBaseCustomerProfileData(-1);
               Customer customer = getBaseCustomer();

               when(customerProfileDataService.save(any()))
                   .thenReturn(customerProfileData.toBuilder()
                       .id(111L)
                       .build());

               Customer createdCustomer = customerProfileService.createCustomer(customer);

               expect(createdCustomer).toEqual(customer.toBuilder().id(111).build());
            });
        });

        describe("update a customer", () -> {
            it("Should update the customer data entity from request", () -> {
                long customerId = 111L;
                Customer customer = getBaseCustomer();
                customer.setId(111);
                customer.setFirstName("Test change");
                customer.setEmail("xyz@gmail.com");

                Optional<CustomerProfileData> customerProfileData =
                    Optional.of(getBaseCustomerProfileData(customerId));

                when(customerProfileDataService.findById(customerId)).thenReturn(customerProfileData);

                when(customerProfileDataService.save(any()))
                    .thenReturn(customerProfileData.get().toBuilder()
                        .firstName("Test change")
                        .email("xyz@gmail.com")
                        .build());

                Customer updatedCustomer = customerProfileService.updateCustomer(customer, customerId);


                verify(customerProfileDataService, times(1)).findById(customerId);
                verify(customerProfileDataService, times(1)).save(any());

                expect(updatedCustomer).toEqual(customer);
            });

            it("should throw Customer Not found when id is not found", () -> {
                long customerId = 111L;
                when(customerProfileDataService.findById(customerId)).thenReturn(Optional.empty());
                expect(() -> {
                    customerProfileService.updateCustomer(any(), customerId);
                }).toThrow(CustomerNotFound.class);
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

    private CustomerProfileData getBaseCustomerProfileData(long customerId) {
        return CustomerProfileData.builder()
            .id(customerId)
            .firstName("Test")
            .lastName("John")
            .dateOfBirth("11-11-2000")
            .homeAddress("02020202")
            .officeAddress("04040404")
            .email("abc@email")
            .build();
    }
}

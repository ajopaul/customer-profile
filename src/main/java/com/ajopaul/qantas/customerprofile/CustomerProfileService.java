package com.ajopaul.qantas.customerprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ajopaul.qantas.customerprofile.CustomerNotFound.CUSTOMER_NOT_FOUND;

@Service
public class CustomerProfileService {

    @Autowired
    private CustomerProfileDataService customerProfileDataService;

    public List<Customer> getAllCustomers() {
        return customerProfileDataService.findAll()
            .stream()
            .map(CustomerProfileService::buildCustomer)
            .collect(Collectors.toList());
    }

    public Customer getCustomerProfile(Long id) {
        return customerProfileDataService
            .findById(id)
            .map(CustomerProfileService::buildCustomer)
            .orElseThrow((() -> CustomerNotFound.builder().id(id).shortMessage(CUSTOMER_NOT_FOUND).build()));
    }

    public void deleteCustomer(Long id) {
        CustomerProfileData customerProfileData = customerProfileDataService.findById(id)
            .orElseThrow(() -> CustomerNotFound.builder().id(id).shortMessage(CUSTOMER_NOT_FOUND).build());

        customerProfileDataService.delete(customerProfileData);
    }

    public Customer createCustomer(Customer customer) {
        CustomerProfileData customerProfileData = buildCustomerProfileData(customer);
        CustomerProfileData createdCustomerProfileData = customerProfileDataService.save(customerProfileData);

        return buildCustomer(createdCustomerProfileData);
    }

    public Customer updateCustomer(Customer customer, long id) {

        CustomerProfileData customerProfileData = customerProfileDataService
                .findById(id)
                .orElseThrow(() -> CustomerNotFound.builder()
                        .id(id)
                        .shortMessage(CUSTOMER_NOT_FOUND)
                        .build());

        customerProfileData.setFirstName(customer.getFirstName());
        customerProfileData.setLastName(customer.getLastName());
        customerProfileData.setDateOfBirth(customer.getDateOfBirth());
        customerProfileData.setHomeAddress(customer.getHomeAddress());
        customerProfileData.setOfficeAddress(customer.getOfficeAddress());
        customerProfileData.setEmail(customer.getEmail());

        return buildCustomer(customerProfileDataService.save(customerProfileData));
    }

    private static Customer buildCustomer(CustomerProfileData customerProfileData){
        return Customer.builder()
            .firstName(customerProfileData.getFirstName())
            .lastName(customerProfileData.getLastName())
            .dateOfBirth(customerProfileData.getDateOfBirth())
            .homeAddress(customerProfileData.getHomeAddress())
            .officeAddress(customerProfileData.getOfficeAddress())
            .email(customerProfileData.getEmail())
            .build();
    }

    private static CustomerProfileData buildCustomerProfileData(Customer customer) {
        return CustomerProfileData.builder()
            .firstName(customer.getFirstName())
            .lastName(customer.getLastName())
            .dateOfBirth(customer.getDateOfBirth())
            .homeAddress(customer.getHomeAddress())
            .officeAddress(customer.getOfficeAddress())
            .email(customer.getEmail())
            .build();
    }
}

package com.ajopaul.qantas.customerprofile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerProfileDataServiceTest {
    @Mock
    CustomerProfileDataService customerProfileDataService;

    @Mock
    CustomerProfileService customerProfileService;

    @Test
    public void testGetAllCustomers() {
        List<CustomerProfileData> customerProfileDataList = asList(CustomerProfileData.builder()
                        .id(111L)
                        .firstName("Test")
                        .lastName("John")
                        .build(),
                CustomerProfileData.builder()
                        .id(222L)
                        .firstName("Test2")
                        .lastName("Woo")
                        .build());
        when(customerProfileDataService.findAll()).thenReturn(customerProfileDataList);
        verify(customerProfileDataService, times(1)).findAll();

        assertEquals(asList(Customer.builder()
                .firstName("Test")
                .lastName("John")
                .build(), Customer.builder()
                .firstName("Test2")
                .lastName("Woo")
                .build()), customerProfileService.getAllCustomers());
    }

}

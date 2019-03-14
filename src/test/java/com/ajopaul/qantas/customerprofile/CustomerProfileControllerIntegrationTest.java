package com.ajopaul.qantas.customerprofile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import static org.hamcrest.CoreMatchers.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CustomerProfileControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateCustomer() {

        ResponseEntity<ResponseData<Customer>> responseEntity =
                createCustomer(Customer.builder().firstName("foo").build());

        ResponseData<Customer> responseData = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Customer customer = responseData.getData();

        assertEquals("foo", customer.getFirstName());
        assertThat(customer.getId(), notNullValue());
    }

    @Test
    public void testGetCustomers() {
        int originalSize = getCustomers().size();

        Customer customer1 = createCustomer(Customer.builder().firstName("foo").build()).getBody().getData();
        Customer customer2 = createCustomer(Customer.builder().firstName("bar").build()).getBody().getData();
        List<Customer> customers = getCustomers();

        assertEquals(originalSize + 2, customers.size());

        assertTrue(customers
                .stream()
                .anyMatch((c -> c.getId().equals(customer1.getId()) && c.getFirstName().equals("foo"))));
        assertTrue(customers
                .stream()
                .anyMatch((c -> c.getId().equals(customer2.getId()) && c.getFirstName().equals("bar"))));
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer1 = Customer.builder().firstName("foo").build();
        Customer customer2 = Customer.builder().firstName("bar").build();

        int customer1Id = createCustomer(customer1)
                .getBody()
                .getData()
                .getId();
        int customer2Id = createCustomer(customer2)
                .getBody()
                .getData()
                .getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Customer> entity = new HttpEntity<>(customer1
                .toBuilder()
                .firstName("foo changed")
                .lastName("foo lastname")
                .build(), headers);
        restTemplate.exchange(
                "/api/customers/"+customer1Id,
                HttpMethod.PUT,
                entity,
                new ParameterizedTypeReference<ResponseData<Customer>>(){});

        List<Customer> customers = getCustomers();

        assertEquals("foo changed", customers
                .stream()
                .filter(c -> c.getId().equals(customer1Id))
                .findAny()
                .get()
                .getFirstName());

        assertEquals("foo lastname", customers
                .stream()
                .filter(c -> c.getId().equals(customer1Id))
                .findAny()
                .get()
                .getLastName());

        assertEquals("bar", customers.stream()
                .filter(c -> c.getId().equals(customer2Id))
                .findAny()
                .get()
                .getFirstName());

        assertNull(customers
                .stream()
                .filter(c -> c.getId().equals(customer2Id))
                .findAny()
                .get()
                .getLastName());
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer1 = Customer.builder().firstName("foo").build();
        createCustomer(customer1);
        Customer customer2 = Customer.builder().firstName("bar").build();
        int customer2Id = createCustomer(customer2).getBody().getData().getId();

        int originalCustomersSize = getCustomers().size();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Customer> entity = new HttpEntity<>(customer2, headers);
        restTemplate.exchange(
                "/api/customers/"+customer2Id,
                HttpMethod.DELETE,
                entity,
                new ParameterizedTypeReference<ResponseData<Customer>>(){});

        List<Customer> customers = getCustomers();
        assertEquals(originalCustomersSize - 1, customers.size());

        assertEquals("foo", customers.get(0).getFirstName());
    }

    private List<Customer> getCustomers() {
        ResponseEntity<ResponseData<List<Customer>>> responseEntity =
                restTemplate.exchange("/api/customers", HttpMethod.GET,
                        null, new ParameterizedTypeReference<ResponseData<List<Customer>>>(){});

        return responseEntity.getBody().getData();
    }

    private ResponseEntity<ResponseData<Customer>> createCustomer(Customer customer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);
        return restTemplate.exchange(
                "/api/customers",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<ResponseData<Customer>>(){});

    }
}
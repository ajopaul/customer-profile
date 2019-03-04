package com.ajopaul.qantas.customerprofile;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Data
@Builder
public class CustomerNotFound extends RuntimeException{

    public static final String CUSTOMER_NOT_FOUND = "Customer Not Found";

    private Long id;
    private String shortMessage;

    public CustomerNotFound(Long id, String shortMessage) {
        super("Customer not found for id: "+id);
        this.id = id;
        this.shortMessage = shortMessage;
    }
}

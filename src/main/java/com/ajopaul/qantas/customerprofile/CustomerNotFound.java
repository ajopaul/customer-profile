package com.ajopaul.qantas.customerprofile;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Data
@Builder
public class CustomerNotFound extends RuntimeException{

    private Long id;

    public CustomerNotFound(Long id) {
        super("Customer profile not found for id: "+id);
        this.id = id;
    }
}

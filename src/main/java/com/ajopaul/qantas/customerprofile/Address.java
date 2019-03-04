package com.ajopaul.qantas.customerprofile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private String home;
    private String office;
    private String email;
}

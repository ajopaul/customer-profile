package com.ajopaul.qantas.customerprofile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String homeAddress;
    private String officeAddress;
    private String email;
}

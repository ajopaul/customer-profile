package com.ajopaul.qantas.customerprofile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private Integer id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String homeAddress;
    private String officeAddress;
    private String email;
}

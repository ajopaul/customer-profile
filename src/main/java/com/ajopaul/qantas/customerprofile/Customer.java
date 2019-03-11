package com.ajopaul.qantas.customerprofile;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @ApiModelProperty(notes = "The database generated customer ID")
    private Integer id;
    @ApiModelProperty(notes = "First name of the customer")
    private String firstName;
    @ApiModelProperty(notes = "Last name of the customer")
    private String lastName;
    @ApiModelProperty(notes = "The date of birth of the customer in DD-MM-YYYY format eg: 01-01-1970")
    private String dateOfBirth;
    @ApiModelProperty(notes = "The home address in single line")
    private String homeAddress;
    @ApiModelProperty(notes = "The office address in single line")
    private String officeAddress;
    @ApiModelProperty(notes = "The email of the customer (eg:- abc@xyz.com)")
    private String email;
}

package com.ajopaul.qantas.customerprofile;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
public class CustomerProfileData {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private Address address;
}

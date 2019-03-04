package com.ajopaul.qantas.customerprofile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerProfileDataService extends JpaRepository<CustomerProfileData, Long> {

}

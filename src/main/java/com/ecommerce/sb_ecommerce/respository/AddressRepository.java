package com.ecommerce.sb_ecommerce.respository;

import com.ecommerce.sb_ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {


}

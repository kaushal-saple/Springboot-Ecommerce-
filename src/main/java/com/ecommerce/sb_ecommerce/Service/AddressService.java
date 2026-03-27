package com.ecommerce.sb_ecommerce.Service;

import com.ecommerce.sb_ecommerce.model.Address;
import com.ecommerce.sb_ecommerce.model.User;
import com.ecommerce.sb_ecommerce.payload.AddressDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO addAddress(AddressDTO addressDTO, User user);
    List<AddressDTO> getAllAddress();

    AddressDTO getAddress(Long addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}

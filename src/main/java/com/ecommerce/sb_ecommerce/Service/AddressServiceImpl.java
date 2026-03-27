package com.ecommerce.sb_ecommerce.Service;

import com.ecommerce.sb_ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecommerce.model.Address;
import com.ecommerce.sb_ecommerce.model.User;
import com.ecommerce.sb_ecommerce.payload.AddressDTO;
import com.ecommerce.sb_ecommerce.respository.AddressRepository;
import com.ecommerce.sb_ecommerce.respository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;


    @Override
    public AddressDTO addAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);

        user.setAddresses(addressList);
        address.setUser(user);

        addressRepository.save(address);
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddress() {
        List<Address>  addresses = addressRepository.findAll();

        return addresses.stream().map(address->
                modelMapper.map(address,AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO getAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address","AddressId",addressId));

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addresses = user.getAddresses();
        return  addresses.stream().map(address->modelMapper.map(address,AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address addressFromDB = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address","AddressId",addressId));

        addressFromDB.setCity(addressDTO.getCity());
        addressFromDB.setCountry(addressDTO.getCountry());
        addressFromDB.setPincode(addressDTO.getPincode());
        addressFromDB.setStreet(addressDTO.getStreet());
        addressFromDB.setBuildingName(addressDTO.getBuildingName());
        addressFromDB.setState(addressDTO.getState());

        Address updatedAddress = addressRepository.save(addressFromDB);
        User user = addressFromDB.getUser();
        user.getAddresses().removeIf(address->address.getAddressId().equals(updatedAddress.getAddressId()));
        user.getAddresses().add(updatedAddress);

        userRepository.save(user);
        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDB = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address","AddressId",addressId));

        User user = addressFromDB.getUser();
        user.getAddresses().removeIf(address->address.getAddressId().equals(addressId));


        userRepository.save(user);

        addressRepository.delete(addressFromDB);
        return "Address with AddressId " + addressId +" deleted successfully";
    }
}

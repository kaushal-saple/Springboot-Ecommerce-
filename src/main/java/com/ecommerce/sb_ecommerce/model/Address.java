package com.ecommerce.sb_ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data

@NoArgsConstructor
@Table(name = "addresses")
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 3 , message = "Street name must be least 3 characters")
    private String street;

    @NotBlank
    @Size(min = 3 , message = "Building name must be least 3 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 3 , message = "City name must be least 3 characters")
    private String city;

    @NotBlank
    @Size(min = 3 , message = "State name must be least 3 characters")
    private String State;

    @NotBlank
    @Size(min = 3 , message = "Country name must be least 3 characters")
    private String country;

    @NotBlank
    @Size(min = 3 , message = "Pincode  must be least 6 digit")
    private String pincode;

    public Address(Long addressId, String buildingName, String street, String city, String state, String country, String pincode) {
        this.addressId = addressId;
        this.buildingName = buildingName;
        this.street = street;
        this.city = city;
        State = state;
        this.country = country;
        this.pincode = pincode;
    }




}

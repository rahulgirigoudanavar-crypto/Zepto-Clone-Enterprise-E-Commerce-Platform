package com.zepto.address.dto;

import java.time.LocalDateTime;

import com.zepto.address.entity.AddressType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

    private Long id;

    private Long userId;

    private String fullName;

    private String phoneNumber;

    private String houseNumber;

    private String street;

    private String landmark;

    private String city;

    private String state;

    private String pincode;

    private AddressType addressType;

    private Boolean defaultAddress;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
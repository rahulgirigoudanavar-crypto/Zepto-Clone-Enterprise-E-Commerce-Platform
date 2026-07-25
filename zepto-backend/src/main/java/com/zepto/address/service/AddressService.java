package com.zepto.address.service;

import java.util.List;

import com.zepto.address.dto.AddressRequest;
import com.zepto.address.dto.AddressResponse;

public interface AddressService {

    AddressResponse createAddress(AddressRequest request);

    AddressResponse updateAddress(Long addressId, AddressRequest request);

    AddressResponse getAddressById(Long addressId);

    List<AddressResponse> getAddressesByUser(Long userId);

    AddressResponse setDefaultAddress(Long addressId);

    void deleteAddress(Long addressId);

}
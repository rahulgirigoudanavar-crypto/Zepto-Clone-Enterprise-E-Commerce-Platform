package com.zepto.address.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zepto.address.dto.AddressRequest;
import com.zepto.address.dto.AddressResponse;
import com.zepto.address.entity.Address;
import com.zepto.address.repository.AddressRepository;
import com.zepto.user.entity.User;
import com.zepto.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressResponse createAddress(AddressRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(request.getDefaultAddress())) {
            addressRepository.findByUserAndDefaultAddressTrue(user)
                    .forEach(address -> {
                        address.setDefaultAddress(false);
                        addressRepository.save(address);
                    });
        }

        Address address = Address.builder()
                .user(user)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .houseNumber(request.getHouseNumber())
                .street(request.getStreet())
                .landmark(request.getLandmark())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .addressType(request.getAddressType())
                .defaultAddress(
                        Boolean.TRUE.equals(request.getDefaultAddress()))
                .active(true)
                .build();

        Address savedAddress = addressRepository.save(address);

        return mapToResponse(savedAddress);
    }

    @Override
    public AddressResponse updateAddress(Long addressId,
            AddressRequest request) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (Boolean.TRUE.equals(request.getDefaultAddress())) {

            addressRepository.findByUserAndDefaultAddressTrue(address.getUser())
                    .forEach(existingAddress -> {
                        existingAddress.setDefaultAddress(false);
                        addressRepository.save(existingAddress);
                    });
        }

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setHouseNumber(request.getHouseNumber());
        address.setStreet(request.getStreet());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setAddressType(request.getAddressType());
        address.setDefaultAddress(
                Boolean.TRUE.equals(request.getDefaultAddress()));

        Address updatedAddress = addressRepository.save(address);

        return mapToResponse(updatedAddress);
    }

    @Override
    public AddressResponse getAddressById(Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return mapToResponse(address);
    }

    @Override
    public List<AddressResponse> getAddressesByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return addressRepository.findByUserAndActiveTrue(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AddressResponse setDefaultAddress(Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        User user = address.getUser();

        addressRepository.findByUserAndDefaultAddressTrue(user)
                .forEach(existingAddress -> {
                    existingAddress.setDefaultAddress(false);
                    addressRepository.save(existingAddress);
                });

        address.setDefaultAddress(true);

        Address updatedAddress = addressRepository.save(address);

        return mapToResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setActive(false);

        if (Boolean.TRUE.equals(address.getDefaultAddress())) {
            address.setDefaultAddress(false);
        }

        addressRepository.save(address);
    }

    private AddressResponse mapToResponse(Address address) {

        return AddressResponse.builder()
                .id(address.getId())
                .userId(address.getUser().getId())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .houseNumber(address.getHouseNumber())
                .street(address.getStreet())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .addressType(address.getAddressType())
                .defaultAddress(address.getDefaultAddress())
                .active(address.getActive())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }
}

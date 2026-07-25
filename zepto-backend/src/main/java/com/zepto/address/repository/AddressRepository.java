package com.zepto.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zepto.address.entity.Address;
import com.zepto.user.entity.User;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    List<Address> findByUserAndActiveTrue(User user);

    List<Address> findByUserAndDefaultAddressTrue(User user);

}
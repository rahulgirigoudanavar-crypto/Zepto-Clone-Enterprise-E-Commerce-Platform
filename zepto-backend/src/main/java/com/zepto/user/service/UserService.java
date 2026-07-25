package com.zepto.user.service;

import java.util.List;

import com.zepto.user.dto.UserRequest;
import com.zepto.user.dto.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

    void enableUser(Long id);

    void disableUser(Long id);
}
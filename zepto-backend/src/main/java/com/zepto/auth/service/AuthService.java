package com.zepto.auth.service;

import com.zepto.auth.dto.AuthResponse;
import com.zepto.auth.dto.LoginRequest;
import com.zepto.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}
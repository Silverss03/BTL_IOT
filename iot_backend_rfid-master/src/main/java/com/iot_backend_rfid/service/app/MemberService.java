package com.iot_backend_rfid.service.app;

import com.iot_backend_rfid.dto.request.LoginRequest;
import com.iot_backend_rfid.dto.request.RegisterRequest;
import com.iot_backend_rfid.dto.response.LoginResponse;
import com.iot_backend_rfid.dto.response.RegisterResponse;

public interface MemberService {
    LoginResponse memberLogin(LoginRequest loginRequest) throws Exception;
    RegisterResponse register(RegisterRequest registerRequest);
}

package com.iot_backend_rfid.controller;

import com.iot_backend_rfid.dto.request.LoginRequest;
import com.iot_backend_rfid.dto.request.RegisterRequest;
import com.iot_backend_rfid.dto.response.LoginResponse;
import com.iot_backend_rfid.dto.response.RegisterResponse;
import com.iot_backend_rfid.dto.response.Response;
import com.iot_backend_rfid.service.app.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> memberLogin(@RequestBody LoginRequest loginRequest) throws Exception {
        LoginResponse loginResponse=memberService.memberLogin(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(Response.<LoginResponse>builder()
                .data(loginResponse)
                .message("Đăng nhập thành công")
                .success(true)
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<Response<RegisterResponse>> register(@RequestBody RegisterRequest request) throws Exception {
        RegisterResponse registerResponse=memberService.register(request);
        return ResponseEntity.status(HttpStatus.OK).body(Response.<RegisterResponse>builder()
                .data(registerResponse)
                .message("Đăng kí thành công")
                .success(true)
                .build());
    }
}

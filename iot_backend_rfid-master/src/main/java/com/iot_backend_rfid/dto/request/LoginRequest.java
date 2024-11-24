package com.iot_backend_rfid.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Yêu cầu tên đăng nhập")
    private String username;

    @NotBlank(message = "Yêu cầu mật khẩu")
    private String password;
}

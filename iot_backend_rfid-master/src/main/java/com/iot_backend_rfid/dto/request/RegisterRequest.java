package com.iot_backend_rfid.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "Yeu cau ten dang nhap")
    private String username;

    @NotEmpty(message = "Yeu cau mat khau")
    private String password;

    @NotEmpty(message = "Yeu cau ten day du")
    private String name;
    private String memberCode;

    @NotNull(message = "Hay them quyen cho nguoi nay")
    private Integer roleId;
}

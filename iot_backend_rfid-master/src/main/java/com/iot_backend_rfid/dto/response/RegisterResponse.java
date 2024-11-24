package com.iot_backend_rfid.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.iot_backend_rfid.model.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterResponse {
    private String username;
    private String password;
    private String name;
    private String memberCode;
    private Role role;
}

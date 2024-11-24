package com.iot_backend_rfid.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.iot_backend_rfid.model.Role;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String token;
    private Role role;
}

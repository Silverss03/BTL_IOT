package com.iot_backend_rfid.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceResponse {
    private Integer deviceId;
    private String deviceName;
    private String deviceStatus;
    private RoomResponse room;
}

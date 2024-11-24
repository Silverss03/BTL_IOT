package com.iot_backend_rfid.controller;

import com.iot_backend_rfid.common.Constants;
import com.iot_backend_rfid.dto.response.DeviceResponse;
import com.iot_backend_rfid.dto.response.Response;
import com.iot_backend_rfid.service.app.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @PutMapping("/update_device_status/{deviceId}")
    public ResponseEntity<Response<DeviceResponse>> updateDeviceStatus(@PathVariable("deviceId") Integer deviceId){
        DeviceResponse response=deviceService.updateDeviceStatus(deviceId);
        return ResponseEntity.status(HttpStatus.OK).body(Response.<DeviceResponse>builder()
                        .success(true)
                        .message(Constants.UPDATE_SUCCESSFULLY)
                        .data(response)
                .build());
    }
}

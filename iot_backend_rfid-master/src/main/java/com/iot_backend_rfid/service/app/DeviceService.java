package com.iot_backend_rfid.service.app;

import com.iot_backend_rfid.dto.response.DeviceResponse;

public interface DeviceService {
    DeviceResponse updateDeviceStatus(Integer deviceId);
}

package com.iot_backend_rfid.service.app;

import com.iot_backend_rfid.dto.response.DeviceResponse;
import com.iot_backend_rfid.dto.response.RoomResponse;
import com.iot_backend_rfid.exception.AppException;
import com.iot_backend_rfid.model.Device;
import com.iot_backend_rfid.model.Room;
import com.iot_backend_rfid.repository.DeviceRepository;
import com.iot_backend_rfid.service.mqtt.MqttService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final MqttService mqttService;

    @Override
    public DeviceResponse updateDeviceStatus(Integer deviceId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(
                () -> new AppException("Not found device", HttpStatus.NOT_FOUND)
        );
        Boolean deviceStatus = device.getDeviceStatus();
        device.setDeviceStatus(deviceStatus ? false : true);

        Device deviceSave = deviceRepository.saveAndFlush(device);
        mqttService.updateDeviceStatus(deviceSave.getDeviceStatus());

        Room room = deviceSave.getRoom();
        return DeviceResponse.builder()
                .deviceId(deviceSave.getDeviceId())
                .deviceName(deviceSave.getDeviceName())
                .deviceStatus(deviceSave.getDeviceStatus() ? "Active" : "InActive")
                .room(RoomResponse.builder()
                        .roomId(room.getRoomId())
                        .roomCode(room.getRoomCode())
                        .roomName(room.getRoomName())
                        .build())
                .build();
    }
}

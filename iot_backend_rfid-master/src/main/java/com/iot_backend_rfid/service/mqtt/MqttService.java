package com.iot_backend_rfid.service.mqtt;

import com.iot_backend_rfid.common.Constants;
import com.iot_backend_rfid.service.app.AttendanceService;
import lombok.SneakyThrows;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

@Service
public class MqttService implements MqttCallback {

    private MqttClient mqttClient;
    private AttendanceService attendanceService;

    public MqttService(AttendanceService attendanceService) throws MqttException {
        this.attendanceService = attendanceService;
        mqttClient = new MqttClient(Constants.TLS_MQTT_URL, Constants.CLIENT_ID);
        mqttClient.setCallback(this);
        mqttClient.connect();
//        mqttClient = new MqttClient(Constants.TLS_MQTT_URL, Constants.CLIENT_ID);
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setCleanSession(true);
//        options.setUserName(Constants.USERNAME);
//        options.setPassword(Constants.PASSWORD.toCharArray());
//        options.setKeepAliveInterval(60);
//
//        mqttClient.setCallback(this);
//        mqttClient.connect(options);

        mqttClient.subscribe(Constants.TOPIC_RFID);

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String payload = new String(mqttMessage.getPayload());
        System.out.println("Received message: " + payload + " from topic: " + topic);

        if (topic.equals(Constants.TOPIC_RFID)) {
            String resultMessage = attendanceService.checkTakeAttendance(payload);
            MqttMessage sendResult = new MqttMessage(resultMessage.getBytes());
            mqttClient.publish(Constants.TOPIC_LCD, sendResult);
        }


    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @SneakyThrows
    public void updateDeviceStatus(Boolean deviceStatus) {
        String resultMessage = deviceStatus ? Constants.DEVICE_STATUS_ON : Constants.DEVICE_STATUS_OFF;
        MqttMessage sendResult = new MqttMessage(resultMessage.getBytes());
        mqttClient.publish(Constants.TOPIC_DEVICE_STATUS, sendResult);
    }
}

package com.iot_backend_rfid.common;

public class Constants {
    //    public static String TLS_MQTT_URL= "ssl://60900f35fe8e433fbdc7e77aafc036ea.s1.eu.hivemq.cloud:8883";
    public static String TLS_MQTT_URL = "tcp://localhost:1883";
    public static String CLIENT_ID = "nguyenminhquan";
    public static String USERNAME = "nmquan";
    public static String PASSWORD = "Aa123456789";

    public static String TOPIC_RFID = "rfid";
    public static String TOPIC_LCD = "lcd";
    public static String TOPIC_DEVICE_STATUS = "device_status";

    public static String DEVICE_STATUS_ON = "ON";
    public static String DEVICE_STATUS_OFF = "OFF";
    public static String CHECKIN_FAIL = "CHECKIN FAIL";
    public static String CHECKIN_SUCCESSFUL = "CHECKIN_SUCCESSFUL";
    public static String CHECKIN_LATE = " MINUTES LATE";
    public static String UPDATE_SUCCESSFULLY = "Update successfully";
}

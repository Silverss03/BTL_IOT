#include <WiFi.h>              
#include <PubSubClient.h>       
#include <SPI.h>
#include <MFRC522.h>
#include "time.h"
#include <LiquidCrystal_I2C.h>  // Thư viện thời gian

// Thông tin Wi-Fi
const char* ssid = "nhuan";      
const char* wifi_password = "12345678";  
#define SS_PIN 5
#define RST_PIN 2
#define ID_ROOM 4  // ID phòng

// Khai báo chân điều khiển rơ le
#define RELAY1 12
#define RELAY2 13

//
int lcdColumns = 16;
int lcdRows = 2;

// set LCD address, number of columns and rows
// if you don't know your display address, run an I2C scanner sketch
LiquidCrystal_I2C lcd(0x27, lcdColumns, lcdRows);  


// Thông tin MQTT
const char* mqtt_server = "192.168.1.95"; 
const int mqtt_port = 1883;                  


// Khai báo MQTT client và RFID
WiFiClient wifiClient;      
PubSubClient client(wifiClient);  
MFRC522 rfid(SS_PIN, RST_PIN);    
MFRC522::MIFARE_Key key;

byte nuidPICC[4];  
long lastMsg = 0;  

void setup() {
  Serial.begin(115200);           
  setup_wifi();                   
  client.setServer(mqtt_server, mqtt_port);  
  client.setCallback(callback);
  client.subscribe("rfid");
  client.subscribe("lcd");
  client.subscribe("device_status");

  pinMode(RELAY1, OUTPUT);
  pinMode(RELAY2, OUTPUT);

//mặc đinh là 
  digitalWrite(RELAY1, 0);
  digitalWrite(RELAY2, 0);

  // Khởi tạo NTP
  configTime(7 * 3600, 0, "time.nist.gov");  // Múi giờ Việt Nam (+7 GMT)

  SPI.begin();  
  rfid.PCD_Init();  
  
  for (byte i = 0; i < 6; i++) {
    key.keyByte[i] = 0xFF;
  }

  Serial.println(F("This code scans the MIFARE Classic NUID."));

  lcd.begin();        // Với LiquidCrystal_I2C
  lcd.backlight();
}

void setup_wifi() {
  delay(10);
  Serial.print("Connecting to Wi-Fi: ");
  Serial.println(ssid);

  WiFi.begin(ssid, wifi_password);  

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println();
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());


}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect("ESP32Client")) {
      Serial.println("Connected to MQTT broker");
      client.subscribe("rfid");
      client.subscribe("lcd");
    } else {
      Serial.print("Failed to connect, rc=");
      Serial.print(client.state());
      Serial.println(" trying again in 5 seconds...");
      delay(5000);  
    }
  }
}

void LCDNotice(char* notice) {
    static unsigned long startMillis = 0; // Thời điểm bắt đầu hiển thị
    static bool isDisplaying = false;    // Trạng thái hiển thị
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print(notice);
    delay(2000);
    lcd.clear();

}
// Các hàm bật tắt.
void OnRFID(){
  // Tắt rơ le 1
  Serial.println("Tắt rơ le 1");
  digitalWrite(RELAY1, LOW);
}
void OffRFID(){
  Serial.println("Bật rơ le 1");
  digitalWrite(RELAY1, HIGH);
}
void OnLCD(){
  Serial.println("Bật rơ le 2");
  digitalWrite(RELAY2, LOW); 
}
void OffLCD(){
  Serial.println("Tắt rơ le 2");
  digitalWrite(RELAY2, HIGH);
}

void callback(char* topic, byte* message, unsigned int length) {
    // In topic nhận được
    Serial.print("Tin nhắn nhận được từ topic: ");
    Serial.println(topic);
    // In nội dung tin nhắn
    Serial.println();  // Xuống dòng sau khi in xong nội dung

    // Kiểm tra topic
  if (strcmp(topic, "lcd") == 0) {
    Serial.print("Nội dung: ");
      
      // Khai báo mảng chứa tin nhắn
    char notice[100];  // Đảm bảo kích thước đủ lớn

      // Sao chép nội dung tin nhắn vào mảng notice
    for (unsigned int i = 0; i < length && i < sizeof(notice) - 1; i++) {
      Serial.print((char)message[i]);  // In từng ký tự
      notice[i] = (char)message[i];   // Sao chép ký tự
    }
      
    notice[length] = '\0';  // Kết thúc chuỗi bằng ký tự null

    Serial.println();       // Xuống dòng sau khi in nội dung
    Serial.println(notice); // In toàn bộ chuỗi notice

      // Gọi hàm hiển thị lên LCD
    LCDNotice(notice);
  }
// Off RFID
  if (strcmp(topic, "device_status") == 0) {
    Serial.print("Nội dung: ");
      
      // Khai báo mảng chứa tin nhắn
    char notice[100];  // Đảm bảo kích thước đủ lớn

      // Sao chép nội dung tin nhắn vào mảng notice
    for (unsigned int i = 0; i < length && i < sizeof(notice) - 1; i++) {
      Serial.print((char)message[i]);  // In từng ký tự
      notice[i] = (char)message[i];   // Sao chép ký tự
    }
      
    notice[length] = '\0';  // Kết thúc chuỗi bằng ký tự null

    Serial.println();       // Xuống dòng sau khi in nội dung
    Serial.println(notice); // In toàn bộ chuỗi notice

      // Gọi hàm hiển thị lên LCD
    if(strcmp(notice, "OFF:1") == 0){
      OffRFID();
    }
  }
//On RFID
  if (strcmp(topic, "device_status") == 0) {
    Serial.print("Nội dung: ");
      
      // Khai báo mảng chứa tin nhắn
    char notice[100];  // Đảm bảo kích thước đủ lớn

      // Sao chép nội dung tin nhắn vào mảng notice
    for (unsigned int i = 0; i < length && i < sizeof(notice) - 1; i++) {
      Serial.print((char)message[i]);  // In từng ký tự
      notice[i] = (char)message[i];   // Sao chép ký tự
    }
      
    notice[length] = '\0';  // Kết thúc chuỗi bằng ký tự null

    Serial.println();       // Xuống dòng sau khi in nội dung
    Serial.println(notice); // In toàn bộ chuỗi notice

      // Gọi hàm hiển thị lên LCD
    if(strcmp(notice, "ON:1") == 0){
      OnRFID();
    }
  }
// On LCD
  if (strcmp(topic, "device_status") == 0) {
    Serial.print("Nội dung: ");
      
      // Khai báo mảng chứa tin nhắn
    char notice[100];  // Đảm bảo kích thước đủ lớn

      // Sao chép nội dung tin nhắn vào mảng notice
    for (unsigned int i = 0; i < length && i < sizeof(notice) - 1; i++) {
      Serial.print((char)message[i]);  // In từng ký tự
      notice[i] = (char)message[i];   // Sao chép ký tự
    }
      
    notice[length] = '\0';  // Kết thúc chuỗi bằng ký tự null

    Serial.println();       // Xuống dòng sau khi in nội dung
    Serial.println(notice); // In toàn bộ chuỗi notice

      // Gọi hàm hiển thị lên LCD
    if(strcmp(notice, "ON:3") == 0){
      OnLCD();
    }
  }
  // OffLCD
  if (strcmp(topic, "device_status") == 0) {
    Serial.print("Nội dung: ");
      
      // Khai báo mảng chứa tin nhắn
    char notice[100];  // Đảm bảo kích thước đủ lớn

      // Sao chép nội dung tin nhắn vào mảng notice
    for (unsigned int i = 0; i < length && i < sizeof(notice) - 1; i++) {
      Serial.print((char)message[i]);  // In từng ký tự
      notice[i] = (char)message[i];   // Sao chép ký tự
    }
      
    notice[length] = '\0';  // Kết thúc chuỗi bằng ký tự null

    Serial.println();       // Xuống dòng sau khi in nội dung
    Serial.println(notice); // In toàn bộ chuỗi notice

      // Gọi hàm hiển thị lên LCD
    if(strcmp(notice, "OFF:3") == 0){
      OffLCD();
    }
  }
}
void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();  

  if (!rfid.PICC_IsNewCardPresent()) return;
  if (!rfid.PICC_ReadCardSerial()) return;

  Serial.print(F("PICC type: "));
  MFRC522::PICC_Type piccType = rfid.PICC_GetType(rfid.uid.sak);
  Serial.println(rfid.PICC_GetTypeName(piccType));

  if (piccType != MFRC522::PICC_TYPE_MIFARE_MINI && 
      piccType != MFRC522::PICC_TYPE_MIFARE_1K &&
      piccType != MFRC522::PICC_TYPE_MIFARE_4K) {
    Serial.println(F("Your tag is not of type MIFARE Classic."));
    return;
  }

  if (rfid.uid.uidByte[0] != nuidPICC[0] || 
      rfid.uid.uidByte[1] != nuidPICC[1] || 
      rfid.uid.uidByte[2] != nuidPICC[2] || 
      rfid.uid.uidByte[3] != nuidPICC[3]) {
      
    Serial.println(F("A new card has been detected."));

    for (byte i = 0; i < 4; i++) {
      nuidPICC[i] = rfid.uid.uidByte[i];
    }

    Serial.println(F("The NUID tag is:"));
    Serial.print(F("In hex: "));
    printHex(rfid.uid.uidByte, rfid.uid.size);
    Serial.println();

    // Lấy thời gian hiện tại
    struct tm timeinfo;
    if (!getLocalTime(&timeinfo)) {
      Serial.println("Failed to obtain time");
      return;
    }
    Serial.println(&timeinfo, "%A, %B %d %Y %H:%M:%S");  // In thời gian

    // Gửi thông tin ID phòng, NUID thẻ và thời gian lên MQTT Broker
    char payload[150];
    sprintf(payload, "%d&&%02X%02X%02X%02X&&%04d-%02d-%02d %02d:%02d:%02d", 
            ID_ROOM, 
            rfid.uid.uidByte[0], rfid.uid.uidByte[1], rfid.uid.uidByte[2], rfid.uid.uidByte[3], 
            timeinfo.tm_year + 1900, timeinfo.tm_mon + 1, timeinfo.tm_mday, 
            timeinfo.tm_hour, timeinfo.tm_min, timeinfo.tm_sec);
    
    client.publish("rfid", payload);  // Gửi thông tin lên MQTT topic "rfid/idcard"
    Serial.print("Message published: ");
    Serial.println(payload);
  }

  rfid.PICC_HaltA();
  rfid.PCD_StopCrypto1();
}

void printHex(byte *buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], HEX);
  }
}
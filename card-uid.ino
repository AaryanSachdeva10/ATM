#include <SPI.h>
#include <MFRC522.h>

#define SS_PIN 10
#define RST_PIN 9

MFRC522 mfrc522(SS_PIN, RST_PIN); // Create instance

void setup() {
  Serial.begin(57600); // Initialize serial communication
  SPI.begin(); // Initialize SPI communication
  mfrc522.PCD_Init(); // Initialize MFRC522
}

void loop() {
  // Look for new cards
  if (mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial()) {
    for (byte i = 0; i < mfrc522.uid.size; i++) {
      Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
      Serial.print(mfrc522.uid.uidByte[i], HEX);
    }
    Serial.println(); 
    mfrc522.PICC_HaltA(); // Halt PICC
    mfrc522.PCD_StopCrypto1(); // Stop encryption
  }
}
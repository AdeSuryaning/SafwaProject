#include "WiFi.h"
#include <LiquidCrystal_I2C.h>
#include <IOXhop_FirebaseESP32.h>
#include <Wire.h>
#include <ArduinoJson.h>
#include <NTPClient.h>
#include <WiFiUdp.h>
#define LED_BUILTIN 2
#define flowsensor  32
#define flowsensor2  33

#define FIREBASE_HOST "safwaprojek-cea9b-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "eS6Q61uxJe3rkbELAkVmBDSFZ0Dmy4zNvJcKBHD1"

// Define NTP Client to get time
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "id.pool.ntp.org", 25200);
String CurrentTime, CurrentTime2;
char CurrentDate[20], CurrentDate2[25];

//0,403 v, 0,414v, 0,360

//Week Days
String weekDays[7] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

//Month names
String months[12] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

// set the LCD number of columns and rows
int lcdColumns = 16;
int lcdRows = 2;
LiquidCrystal_I2C lcd(0x27, lcdColumns, lcdRows);
const char* ssid = "Kost Putri";
const char* password = "11223344";
//new
const float Offset = 2.350; // const pressure
const float Offset1 = 2.550; // const pressure
//const float Offset2 = 3.599;
float P, Pe, V, Vo; // variabel pressure
//waterflow
volatile int flow_frequency; // Measures flow sensor pulses
volatile int flow_frequency2; // Measures flow sensor pulses

// Calculated litres/hour
float vol = 0.0, l_minute, volDb;
float vol2 = 0.0, l_minute2, volDb2;
//unsigned char flowsensor = 3; // Sensor input pin digital D2
unsigned long currentTime, currentTime2;
unsigned long cloopTime, cloopTime2;
unsigned int tagihan, tagihan2;
int bocor;
float selisih, rerata;
void flow () // Interrupt function
{
  flow_frequency++;
}
void flow2 () // Interrupt function
{
  flow_frequency2++;
}
//for debit only
//void debit ()
//{
//  flow_frequency;
//}
//void debit2 ()
//{
//  flow_frequency2;
//}


void setup()
{
  Serial.begin(115200); //buat serial monitpr pc
  // initialize LCD
  lcd.begin();
  // turn on LCD backlight
  lcd.backlight();
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
    lcd.setCursor(0, 0);
    lcd.print("Connecting WiFi");
  }
  Serial.println("Connected to the WiFi network");
  lcd.setCursor(0, 1);
  lcd.print("Connected!");
  Serial.println("IP address");
  //

  //wf1
  pinMode(flowsensor, INPUT); // inputan flowsensor
  digitalWrite(flowsensor, HIGH); // Optional Internal Pull-Up
  attachInterrupt(digitalPinToInterrupt(flowsensor), flow, RISING); // Setup Interrupt
  //wf2
  pinMode(flowsensor2, INPUT); // inputan flowsensor
  digitalWrite(flowsensor2, HIGH); // Optional Internal Pull-Up
  attachInterrupt(digitalPinToInterrupt(flowsensor2), flow2, RISING); // Setup Interrupt
  currentTime = millis();
  cloopTime = currentTime;
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  timeClient.begin();
  timeClient.setTimeOffset(25200);

}

void loop()
{
  wf();
  wf2();
  pres1();
  pres2();
  //  rerata = (P + Pe) / 2;
  //  Serial.println(rerata);
  //  Serial.print("SELISIH: ");
  //  selisih = V - Vo;
  //  Serial.println(selisih);
  //  delay(1000);
  timeClient.update();

  unsigned long epochTime = timeClient.getEpochTime();
  Serial.print("Epoch Time: ");
  Serial.println(epochTime);

  String formattedTime = timeClient.getFormattedTime();
  Serial.print("Formatted Time: ");
  Serial.println(formattedTime);

  int currentHour = timeClient.getHours();
  Serial.print("Hour: ");
  Serial.println(currentHour);

  int currentMinute = timeClient.getMinutes();
  Serial.print("Minutes: ");
  Serial.println(currentMinute);

  int currentSecond = timeClient.getSeconds();
  Serial.print("Seconds: ");
  Serial.println(currentSecond);

  String weekDay = weekDays[timeClient.getDay()];
  Serial.print("Week Day: ");
  Serial.println(weekDay);

  //Get a time structure
  struct tm *ptm = gmtime ((time_t *)&epochTime);

  int monthDay = ptm->tm_mday;
  Serial.print("Month day: ");
  Serial.println(monthDay);

  int currentMonth = ptm->tm_mon + 1;
  Serial.print("Month: ");
  Serial.println(currentMonth);

  String currentMonthName = months[currentMonth - 1];
  Serial.print("Month name: ");
  Serial.println(currentMonthName);

  int currentYear = ptm->tm_year + 1900;
  Serial.print("Year: ");
  Serial.println(currentYear);

  //Print complete date:
  String currentDate = String(currentYear) + "-" + String(currentMonth) + "-" + String(monthDay);
  Serial.print("Current date: ");
  Serial.println(currentDate);
  //    //LCD Display
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Pre1 = ");
  lcd.setCursor(7, 0);
  lcd.print(l_minute);
  lcd.setCursor(12, 0);
  lcd.print("KPa");
  lcd.setCursor(0, 1);
  lcd.print("Pre2 = ");
  lcd.setCursor(7, 1);
  lcd.print(l_minute2);
  lcd.setCursor(12, 1);
  lcd.print("KPa");
  delay(2000);
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Tag = Rp. ");
  lcd.setCursor(9, 0);
  lcd.print(tagihan);
  lcd.setCursor(0, 1);
  lcd.print("Vol = ");
  lcd.setCursor(6, 1);
  lcd.print(vol);
  lcd.setCursor(10, 1);
  lcd.print(" L");
  delay(1000);
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Status Pipa :");
  //  selisih = P - Pe;
  if (l_minute < 2 || l_minute2 < 2) {
    //        if (pressure2.toInt() <= 3) {
    //      if (pressure1.toInt() || pressure2.toInt() <= 3 || vol.toInt() > 0) {
    lcd.setCursor(0, 1);
    lcd.print("Bocor");
  } else {
    lcd.setCursor(0, 1);
    lcd.print("Aman");
  }
  delay(1000);
  // set value
  Firebase.setString("RealTime/debit", String(vol));
  Firebase.setString("RealTime/pres1", String(l_minute));
  Firebase.setString("RealTime/pres2", String(l_minute2));
  Firebase.setString("RealTime/tagihan", String(tagihan));
  Firebase.setString("RealTime/Date", String(currentDate));
  Firebase.setString("RealTime/Time", String(formattedTime));
  if (l_minute > 2 || l_minute2 > 2) {
    bocor < 2;
    Firebase.setInt("RealTime/kebocoran", bocor);
  } else {
    bocor = 1;
    //    P = 0;
    //    Pe = 0;
    Firebase.setInt("RealTime/kebocoran", bocor);
  }
  char CurrentDate[20];
  String CurrentTime = timeClient.getFormattedTime();
  monthDay = ptm->tm_mday;
  currentMonth = ptm->tm_mon + 1;
  currentYear = ptm->tm_year + 1900;
  sprintf (CurrentDate, "%d%02d%02d", currentYear, currentMonth, monthDay);

  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.createObject();
  root["timestamp"] = String(epochTime);
  root["debit"] = String(vol);
  root["kebocoran"] = String(bocor);
  root["tagihan"] = String(tagihan);
  String history = Firebase.push("/History/" + String(CurrentDate), root);
}

void wf() {
  currentTime = millis();
  // Every second, calculate and print litres/hour
  if (currentTime >= (cloopTime + 1000))
  {
    cloopTime = currentTime; // Updates cloopTime
    if (flow_frequency != 0) {
      // Pulse frequency (Hz) = 7.5Q, Q is flow rate in L/min.
      l_minute = (flow_frequency / 7.5) / 60; // (Pulse frequency x 60 min) / 7.5Q = flowrate in L/hour
      //      l_minute = l_minute / 60; // 1 menit ada 60 dtk
      vol = vol + l_minute; // data pembacaan ditambah rata" pemakaian permenit
      flow_frequency = 0; // Reset Counter
      tagihan = vol * 500; //vol nilai asli yg dibaca dari waterflow, 500 adalah besaran biaya yg ditetapkan petugas
      //      Serial.println(tagihan);
      //      Serial.print(l_minute); // Print litres/hour
      //      Serial.println(" L/Sec");
      Serial.print("Debit1= ");
      Serial.print(l_minute);
      Serial.println(" L");
    }
    else {
      Serial.println(" flow rate = 0 ");
      //      lcd.clear();
      //      lcd.setCursor(0, 0);
      //      lcd.print("Rate: ");
      //      lcd.print( flow_frequency );
      //      lcd.print(" L/M");
      //      lcd.setCursor(0, 1);
      //      lcd.print("Vol:");
      //      lcd.print(vol);
      //      lcd.print(" L");
    }
  }
}
void wf2() {
  currentTime2 = millis();
  Serial.println("debit2 masuk");
  // Every second, calculate and print litres/hour
  if (currentTime2 >= (cloopTime2 + 1000))
  {
    cloopTime2 = currentTime2; // Updates cloopTime
    if (flow_frequency2 != 0) {
      // Pulse frequency (Hz) = 7.5Q, Q is flow rate in L/min.
      l_minute2 = (flow_frequency2 / 7.5) / 60; // (Pulse frequency x 60 min) / 7.5Q = flowrate in L/hour
      //      l_minute = l_minute / 60; // 1 menit ada 60 dtk
      vol2 = vol2 + l_minute2; // data pembacaan ditambah rata" pemakaian permenit
      flow_frequency2 = 0; // Reset Counter
      tagihan2 = vol2 * 500; //vol nilai asli yg dibaca dari waterflow, 500 adalah besaran biaya yg ditetapkan petugas
      //      Serial.println(tagihan2);
      //      Serial.print(l_minute2); // Print litres/hour
      //      Serial.println(" L/Sec");
      Serial.print("Debit2= ");
      Serial.print(l_minute2);
      Serial.println(" L");
    }
    else {
      Serial.println(" flow rate2 = 0 ");
      //      lcd.clear();
      //      lcd.setCursor(0, 0);
      //      lcd.print("Rate: ");
      //      lcd.print( flow_frequency );
      //      lcd.print(" L/M");
      //      lcd.setCursor(0, 1);
      //      lcd.print("Vol:");
      //      lcd.print(vol);
      //      lcd.print(" L");
    }
  }
}

void pres1() {
  V = analogRead(34) * 5.00 / 1024;
  P = (V - Offset) * 400;
  if ( P <= 0) {
    P = 0.00;
  }
  Serial.print("Voltage1:");
  Serial.print(V, 3);
  Serial.println("V");

  Serial.print("Press 1:");
  Serial.print(l_minute, 1);
  Serial.print("KPa");
  Serial.println("------------");
  delay (500);
}
void pres2() {
  Vo = analogRead(35) * 5.00 / 1024;
  Pe = (Vo - Offset1) * 400;
  if ( Pe <= 0) {
    Pe = 0.00;
  }
  Serial.print("Voltage2:");
  Serial.print(Vo, 3);
  Serial.println("V");

  Serial.print("Press 2:");
  Serial.print(l_minute2, 1);
  Serial.print("L");
  Serial.println("------------");
  delay (500);
}

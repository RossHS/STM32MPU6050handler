# STM32MPU6050Handler
REQUIREMENTS
------------

The minimum requirement by STM32MPU6050Handler is that your PC supports Java 8. 
It`s all because the library for working with the serial port does not support new versions of java. 
To verify this, enter the command in the console

    java -version 
    
and that's what you should get:

    java version "1.8.0_172"
    Java(TM) SE Runtime Environment (build 1.8.0_172-b11)
    Java HotSpot(TM) 64-Bit Server VM (build 25.172-b11, mixed mode)

For correct run you should have such boards.

![cp2102-conversor-usb-a-ttl](https://user-images.githubusercontent.com/30704653/40884332-56b6ed9e-671a-11e8-926f-5c981cfbcfa5.jpg)
![mpu6050](https://user-images.githubusercontent.com/30704653/40884333-5882073a-671a-11e8-8b00-4e83ddf519a5.jpg)

    USB serial module connection 6050 module approach is: USB serial module + 5V, TXD, RXD, GND 
    connection VCC 6050 module, RX, TX, GND. Note TXD and RXD cross.

Then you have to be sure that you have installed the cp2102 drivers for USB bridge. 
If they are not installed, you can download them from the link below
  
    https://www.silabs.com/products/development-tools/software/usb-to-uart-bridge-vcp-drivers

QUICK START
---------------
If there are problems when starting through a double click, you should start the program by run the start.bat file 
or write in the console

    java -jar STM32-MPU6050-handler-1.0-SNAPSHOT-jar-with-dependencies.jar

CAPABILITIES
---------------
+ Real time information display on graphics

<img src="https://user-images.githubusercontent.com/30704653/40885201-606ef7f4-672a-11e8-973c-1b5e2f99e042.jpg" width="400" height="400" />

+ Write data from MPU6050 to a file for later analysis

![record](https://user-images.githubusercontent.com/30704653/40885228-f81f6a20-672a-11e8-903a-468597a59ecf.jpg)
![540706f499](https://user-images.githubusercontent.com/30704653/40885235-3e3f3044-672b-11e8-8f2b-154cf054f500.jpg)

And the piece of files that we get:

    .....
    -5.065918 -2.0751953 -0.8544922
    -5.065918 -2.1972656 -0.91552734
    -1.7700195 0.12207031 -1.2207031
    .....
    
    .....
    55 51 DD F9 49 FF 6A 04 AF FC DD 
    55 52 E8 FF 00 00 F6 FF AF FC 2E 
    55 53 3A 07 C2 26 D5 1F AF FC 70 
    55 51 D0 F9 3B FF 55 04 A9 FC A7 
    55 52 AD FF DE FF F2 FF A9 FC C6 
    55 53 35 07 C3 26 D6 1F A9 FC 67 
    .....
    
+ Mouse, controlled by MPU6050

![141e69bc76](https://user-images.githubusercontent.com/30704653/40885274-08e3eaa6-672c-11e8-9468-ea9926ea7b5c.jpg)

+ 3D figure whose position depends on the MPU6050

<img src="https://user-images.githubusercontent.com/30704653/40885349-2aaa4ce2-672d-11e8-97ae-b1587dd65366.jpg" width="400" height="400" />

WHAT`S NEXT
---------------
+ Solve the problem of memory leak associated with the JFreechart library, and a large load on the CPU
+ It is necessary to realize all the remaining possibilities of the Gyro Mouse and create an error correction algorithm
+ Add the ability to customize 3D shapes

---
best regards,
Ross Khapilov BMSTU 2018

If you have any questions, contact me rossxpanteley@gmail.com

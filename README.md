# SerialApi

How to use ?
```Java
1、gradle


allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
  
  dependencies {
	   implementation 'com.github.INTKILOW:SerialApi:1.0.2'
	}
  
2、maven
  
  <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://www.jitpack.io</url>
		</repository>
	</repositories>
  
  <dependency>
	    <groupId>com.github.INTKILOW</groupId>
	    <artifactId>SerialApi</artifactId>
	    <version>1.0.2</version>
	</dependency>
  
  ```
  ```Java
    SerialControl serialControl = SerialControl.getInstance();
    //SerialListener
    //TestSerialListener
    serialControl.setSerialListener(); 
  ```
  
    ```Java
     try {
            SerialPort serialPort = new SerialPort(new File("/dev/ttyS4"),115200,0);

            serialPort.send("你好");
        } catch (Exception e) {
            e.printStackTrace();
        }
  ```

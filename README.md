# Test Automation using WebDrivers

### Reference Documentation
The purpose of this project is just for reference. A simple Spring Boot project which uses Chrome or PhantomJS browser to fetch a given website dom content.

### Guides
The following guides illustrate how to use some features concretely:

Start spring boot project (PhantomjsDemoApplication.java file). Make sure you create a folder `C:\Temp` and place `phantomjs.exe`. Then access end point `http://localhost:8080/api/v1/jobs/execute?browser=phantomjs&url=https://google.com` with dynamic parameters for url variable. 
This project also supports chrome and for this to work create/update `C:\Temp` and place `chromedriver.exe`. 

* [Get google.com page dom source](http://localhost:8080/api/v1/jobs/execute?browser=phantomjs&url=https://google.com)


As required this project can be further extended.

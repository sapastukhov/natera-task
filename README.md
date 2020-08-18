Clone repository to desired folder, than run the following commands from this folder:
1) To run all tests
```
mvn clean test
```
2) To generate report with jetty server
```
mvn allure:serve
```
3) To generate report
```
mvn allure:report
```

Available system properties
1. -Duser.token=tokenUuid
2. -Dapi.url=https://example.com/api

__note:__ You should have to install JDK8 and maven 3+
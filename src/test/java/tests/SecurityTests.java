package tests;

import config.AppConfig;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import utils.MyAllureRestAssured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Feature("Security tests")
public class SecurityTests {

    @Test(description = "401 error returns if sending request without X-User header")
    public void creatingTriangleWithoutSendingXUserHeader(){
        AppConfig config = ConfigFactory.create(AppConfig.class);
        RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri(config.apiUrl())
                .setContentType(ContentType.JSON)
                .addFilter(new MyAllureRestAssured())
                .log(LogDetail.ALL)
                .addFilter(new ResponseLoggingFilter(LogDetail.BODY))
                .build();
        given(requestSpecification).basePath("/triangle").body("{\"input\":\"2;3;4\"}").post().then().statusCode(401)
                .body("status", equalTo(401))
                .body("error", equalTo("Unauthorized"))
                .body("message", equalTo("No message available"));
    }
}

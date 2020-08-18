package fixtures;

import config.AppConfig;
import controllers.TriangleController;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import utils.MyAllureRestAssured;

public class Fixture {
    @BeforeSuite
    public void beforeSuite(){
        AppConfig config = ConfigFactory.create(AppConfig.class);
        RestAssured.requestSpecification = new RequestSpecBuilder().setBaseUri(config.apiUrl())
                .setContentType(ContentType.JSON)
                .addHeader("X-User", config.userToken())
                .addFilter(new MyAllureRestAssured())
                .log(LogDetail.ALL)
                .addFilter(new ResponseLoggingFilter(LogDetail.BODY))
                .build();
    }

    @AfterMethod
    public void deleteAllTriangles(){
        TriangleController.getAll().parallelStream().forEach((triangle) -> TriangleController.deleteById(triangle.getId()));
    }
}

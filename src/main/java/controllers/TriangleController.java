package controllers;

import io.qameta.allure.Step;
import models.ErrorDto;
import models.TriangleDto;
import models.TriangleReqDto;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TriangleController {
    public static String URL = "/triangle";

    private TriangleController(){
    }

    @Step("Create triangle")
    public static TriangleDto create(TriangleReqDto triangleReqDto){
        return given().basePath(URL).body(triangleReqDto).post().then().statusCode(200).extract().response().as(TriangleDto.class);
    }

    @Step("Create triangle with 'Cannot process input' 422 error")
    public static void createWithUnprocessableDataError(TriangleReqDto triangleReqDto){
        given().basePath(URL).body(triangleReqDto).post()
                .then()
                    .statusCode(422)
                    .body("status", equalTo(422))
                    .body("error", equalTo("Unprocessable Entity"))
                    .body("exception", equalTo("com.natera.test.triangle.exception.UnprocessableDataException"))
                    .body("message", equalTo("Cannot process input"));
    }

    @Step("Create triangle with 'Limit exceeded' 422 error")
    public static void createWithLimitExceededError(TriangleReqDto triangleReqDto){
        given().basePath(URL).body(triangleReqDto).post()
                .then()
                .statusCode(422)
                .body("status", equalTo(422))
                .body("error", equalTo("Unprocessable Entity"))
                .body("exception", equalTo("com.natera.test.triangle.exception.LimitExceededException"))
                .body("message", equalTo("Limit exceeded"));
    }

    @Step("Get triangle by correct id {triangleId}")
    public static TriangleDto getById(String triangleId){
        return given().basePath(URL).pathParam("triangleId", triangleId).get("/{triangleId}").then().statusCode(200).extract().response().as(TriangleDto.class);
    }

    @Step("Get triangle by incorrect id {triangleId}")
    public static ErrorDto getByIncorrectId(String triangleId){
        return given().basePath(URL).pathParam("triangleId", triangleId).get("/{triangleId}").then().statusCode(400).extract().response().as(ErrorDto.class);
    }

    @Step("Get triangle by not exist id {triangleId}")
    public static ErrorDto getByNotExistId(String triangleId){
        return given().basePath(URL).pathParam("triangleId", triangleId).get("/{triangleId}").then().statusCode(404).extract().response().as(ErrorDto.class);
    }

    @Step("Delete triangle by id {triangleId}")
    public static void deleteById(String triangleId){
        given().basePath(URL).pathParam("triangleId", triangleId).delete("/{triangleId}").then().statusCode(200);
    }

    @Step("Delete triangle by incorrect id {triangleId}")
    public static ErrorDto deleteByIncorrectId(String triangleId){
        return given().basePath(URL).pathParam("triangleId", triangleId).delete("/{triangleId}").then().statusCode(400).extract().response().as(ErrorDto.class);
    }

    @Step("Delete triangle by not exist id {triangleId}")
    public static ErrorDto deleteByNotExistId(String triangleId){
        return given().basePath(URL).pathParam("triangleId", triangleId).delete("/{triangleId}").then().statusCode(404).extract().response().as(ErrorDto.class);
    }

    @Step("Get all triangles")
    public static List<TriangleDto> getAll(){
        TriangleDto[] array = given().basePath(URL + "/all").get().then().statusCode(200).extract().response().as(TriangleDto[].class);
        return Arrays.asList(array);
    }

    @Step("Get triangle perimeter by id {triangleId}")
    public static double getPerimeterById(String triangleId){
        String result =  given().basePath(URL).pathParam("triangleId", triangleId).get("/{triangleId}/perimeter")
                .then().statusCode(200).body("size()", is(1))
                .extract().response().jsonPath().getString("result");
        return Double.parseDouble(result);
    }

    @Step("Get triangle perimeter by incorrect id {triangleId}")
    public static ErrorDto getPerimeterByIncorrectId(String triangleId){
        return given().basePath(URL).pathParam("triangleId", triangleId).get("/{triangleId}/perimeter")
                .then().statusCode(400).extract().response().as(ErrorDto.class);
    }

    @Step("Get triangle perimeter by not exist id {triangleId}")
    public static ErrorDto getPerimeterByNotExistId(String triangleId){
        return given().basePath(URL).pathParam("triangleId", triangleId).get("/{triangleId}/perimeter")
                .then().statusCode(404).extract().response().as(ErrorDto.class);
    }

    @Step("Get triangle area by id {triangleId}")
    public static double getAreaById(String triangleId){
        String result = given().basePath(URL).pathParam("triangleId", triangleId).get("/{triangleId}/area")
                .then().statusCode(200).body("size()", is(1))
                .extract().response().jsonPath().getString("result");
        return Double.parseDouble(result);
    }

    @Step("Get triangle area by incorrect id {triangleId}")
    public static ErrorDto getAreaByIncorrectId(String triangleId){
        return given().basePath(URL).pathParam("triangleId", triangleId).get("/{triangleId}/area")
                .then().statusCode(400).extract().response().as(ErrorDto.class);
    }

    @Step("Get triangle area by not exist id {triangleId}")
    public static ErrorDto getAreaByNotExistId(String triangleId){
        return given().basePath(URL).pathParam("triangleId", triangleId).get("/{triangleId}/area")
                .then().statusCode(404).extract().response().as(ErrorDto.class);
    }
}

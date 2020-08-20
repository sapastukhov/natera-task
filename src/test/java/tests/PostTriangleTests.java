package tests;

import controllers.TriangleController;
import fixtures.Fixture;
import io.qameta.allure.Feature;
import models.TriangleDto;
import models.TriangleReqDto;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("Triangle creation POST /triangle")
public class PostTriangleTests extends Fixture {
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    //format {input, expected 1st side value, expected 2st side value, expected 3st side value}
    @DataProvider(name = "Positive")
    public static Object[][] positiveValues() {
        return new Object[][]{
                {" 4;4;7","4.0","4.0","7.0"},
                {"4;4;7 ","4.0","4.0","7.0"},
                {" 4;4;7 ","4.0","4.0","7.0"},
                {"4;  4  ;7","4.0","4.0","7.0"},
                {"7;7;5","7.0","7.0","5.0"},
                {"7;5;7","7.0","5.0","7.0"},
                {"5;7;7","5.0","7.0","7.0"},
                {"7;7;7","7.0","7.0","7.0"},
                {"2;3;4","2.0","3.0","4.0"},
                {"9.9e+76;9.9e+76;1","9.9E76","9.9E76","1.0"},
                {"1;9.9e+76;9.9e+76","1.0","9.9E76","9.9E76"},
                {"9.9e+76;1;9.9e+76","9.9E76","1.0","9.9E76"},
                {"4.0;4.0;7","4.0","4.0","7.0"},
                {"4.0;7;4.0","4.0","7.0","4.0"},
                {"7;4.0;4.0","7.0","4.0","4.0"},
                {"4e1;4e1;7e1","40.0","40.0","70.0"},
                {"4e1;7e1;4e1","40.0","70.0","40.0"},
                {"7e1;4e1;4e1","70.0","40.0","40.0"},
                {"-1;3;3","1.0","3.0","3.0"},
                {"3;3;-1","3.0","3.0","1.0"},
                {"3;-1;3","3.0","1.0","3.0"},
                {"0.3;0.3;0.5","0.3","0.3","0.5"},
                {"0.3;0.5;0.3","0.3","0.5","0.3"},
                {"0.5;0.3;0.3","0.5","0.3","0.3"}
        };
    }

    @DataProvider(name = "Negative incorrect input")
    public static Object[][] negativeValues() {
        return new Object[][]{
                {"6;3;1"},
                {"3;6;1"},
                {"3;1;6"},
                {"6A;3;1"},
                {"3;6A;1"},
                {"3;1;6A"},
                {"1;1;2"},
                {"1;2;1"},
                {"2;1;1"},
                {"0;1;1"},
                {"1;0;1"},
                {"1;1;0"},
                {"0;0;0"},
                {"1;1"},
                {"1;1;1;1"},
                {"1"},
                {""},
                {"A;B;C"},
                {"%;/;?"},
                {"06;003;01"},
                {"1e+77;1e+77;1"},
                {"1;1e+77;1e+77"},
                {"1e+77;1;1e+77"}
        };
    }

    @DataProvider(name = "Separators")
    public static Object[][] separators() {
        return new Object[][]{
                {"\\"}, {"|"}, {"!"}, {"#"}, {"$"}, {"%"}, {"&"}, {"/"},
                {"("}, {")"}, {"="}, {"?"}, {"»"}, {"«"}, {"@"}, {"£"},
                {"§"}, {"€"}, {"{"}, {"}"}, {"."}, {"-"}, {"'"}, {"<"},
                {">"}, {"_"}, {","}, {" "}, {"\t"}, {"\n"}, {"\r"}, {"AB"},
                {"ab"}, {"123"}, {"\""}
        };
    }

    @Test(description = "Create triangle with correct input", dataProvider = "Positive", groups = "positive post")
    public void creatingTrianglePositive(String input, String firstSide, String secondSide, String thirdSide){
        TriangleReqDto triangleReqDto = TriangleReqDto.builder().input(input).build();
        TriangleDto triangleDto = TriangleController.create(triangleReqDto);
        assertThat(triangleDto).as("Triangle values").extracting("firstSide", "secondSide", "thirdSide")
                .containsExactly(firstSide, secondSide, thirdSide);
    }

    @Test(description = "Can not create triangle with incorrect input", dataProvider = "Negative incorrect input", groups = "negative post")
    public void creatingTriangleNegative(String input){
        TriangleReqDto triangleReqDto = TriangleReqDto.builder().input(input).build();
        TriangleController.createWithUnprocessableDataError(triangleReqDto);
    }

    @Test(description = "Creating triangle with non default separator", dataProvider = "Separators")
    public void creatingTriangleDifferentSeparators(String separator){
        TriangleReqDto triangleReqDto = TriangleReqDto.builder().separator(separator).input("2"+separator+"3"+separator+"4").build();
        TriangleDto triangleDto = TriangleController.create(triangleReqDto);
        assertThat(triangleDto).as("Triangle values").extracting("firstSide", "secondSide", "thirdSide")
                .containsExactly("2.0", "3.0", "4.0");
    }

    @Test(description = "Creating triangle without not mandatory 'separator' field")
    public void creatingTriangleWithoutSeparatorField(){
        TriangleDto triangleDto = given().basePath("/triangle").body("{\"input\":\"2;3;4\"}").post().then().statusCode(200).extract().response().as(TriangleDto.class);
        assertThat(triangleDto).as("Triangle values").extracting("firstSide", "secondSide", "thirdSide")
                .containsExactly("2.0", "3.0", "4.0");
    }

    @Test(description = "Receiving 'Bad request' when creating triangle with empty 'separator' field")
    public void creatingTriangleWithEmptySeparatorField(){
        given().basePath("/triangle").body("{\"separator\":\"\", \"input\":\"233243\"}").post().then().statusCode(400);
    }

    @Test(description = "Returning 'Bad request' when creating triangle without mandatory 'input' field")
    public void creatingTriangleWithoutMandatoryInputField(){
        given().basePath("/triangle").body("{\"separator\":\";\"}").post().then().statusCode(400);
    }

    @Test(description = "Can create 10 triangles for one user")
    public void createMaximumTriangles(){
        TriangleReqDto triangleReqDto = TriangleReqDto.builder().input("2;3;4").build();
        for (int i=0; i<10;i++) {
            executorService.submit(() -> {
                TriangleController.create(triangleReqDto);
            });
        }
        awaitTerminationAfterShutdown(executorService);
    }

    @Test(description = "Can not create more than 10 triangles for one user")
    public void createMoreThanMaximumTriangles(){
        TriangleReqDto triangleReqDto = TriangleReqDto.builder().input("2;3;4").build();
        for (int i=0; i<10;i++) {
            TriangleController.create(triangleReqDto);
        }
        TriangleController.createWithLimitExceededError(triangleReqDto);
    }
}

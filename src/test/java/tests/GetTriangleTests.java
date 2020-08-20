package tests;

import controllers.TriangleController;
import fixtures.Fixture;
import io.qameta.allure.Feature;
import models.ErrorDto;
import models.TriangleDto;
import models.TriangleReqDto;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Triangle getting GET /triangle")
public class GetTriangleTests extends Fixture {

    @DataProvider(name = "Area Values")
    public static Object[][] areaValues() {
        return new Object[][]{
                {"5.0;7.0;7.0",16.345871},
                {"7;7;7", 21.217623},
                {"2e1;3e1;4e1", 290.47375},
                {"9.9e+76;9.9e+76;9.9e+76", 4.243957491245641E153},
        };
    }

    @DataProvider(name = "Perimeter Values")
    public static Object[][] perimeterValues() {
        return new Object[][]{
                {"5.0;7.0;7.0",19.0},
                {"7;7;7", 21.0},
                {"2e1;3e1;4e1", 90.0},
                {"9.9e+76;9.9e+76;9.9e+76", 2.97E77},
        };
    }

    @Test(description = "Check area value of triangle", dataProvider = "Area Values")
    public void checkTriangleArea(String input, double expectedResult){
        TriangleReqDto triangleReqDto = TriangleReqDto.builder().input(input).build();
        TriangleDto triangleDto = TriangleController.create(triangleReqDto);
        double result = TriangleController.getAreaById(triangleDto.getId());
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test(description = "Check perimeter value of triangle", dataProvider = "Perimeter Values")
    public void checkTrianglePerimeter(String input, double expectedResult){
        TriangleReqDto triangleReqDto = TriangleReqDto.builder().input(input).build();
        TriangleDto triangleDto = TriangleController.create(triangleReqDto);
        double result = TriangleController.getPerimeterById(triangleDto.getId());
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test(description = "Get triangle by existing id")
    public void getTriangleByExistingId(){
        TriangleReqDto triangleReqDto = TriangleReqDto.builder().input("2;3;4").build();
        TriangleDto triangleDto = TriangleController.create(triangleReqDto);
        triangleDto = TriangleController.getById(triangleDto.getId());
        assertThat(triangleDto).as("Triangle values").extracting("firstSide", "secondSide", "thirdSide")
                .containsExactly("2.0", "3.0", "4.0");
    }

    @Test(description = "Bad Request error when try to get triangle by incorrect id")
    public void getTriangleByIncorrectId(){
        TriangleController.getByIncorrectId("1");
    }

    @Test(description = "Not found error when try to get triangle by not exist id")
    public void getTriangleByNotExistId(){
        ErrorDto errorDto = TriangleController.getByNotExistId(UUID.randomUUID().toString());
        assertThat(errorDto).extracting("status", "error", "exception", "message")
                .containsExactly(404, "Not Found", "com.natera.test.triangle.exception.NotFounException", "Not Found");
    }

    @Test(description = "Bad request error when try to get triangle perimeter by incorrect id")
    public void getTrianglePerimeterByIncorrectId(){
        TriangleController.getPerimeterByIncorrectId("132-23");
    }

    @Test(description = "Bad request error when try to get triangle area by incorrect id")
    public void getTriangleAreaByIncorrectId(){
        TriangleController.getAreaByIncorrectId("1232-23");
    }

    @Test(description = "Not found error when try to get triangle perimeter by not exist id")
    public void getTrianglePerimeterByNotExistId(){
        ErrorDto errorDto = TriangleController.getPerimeterByNotExistId(UUID.randomUUID().toString());
        assertThat(errorDto).extracting("status", "error", "exception", "message")
                .containsExactly(404, "Not Found", "com.natera.test.triangle.exception.NotFounException", "Not Found");
    }

    @Test(description = "Not found error when try to get triangle area by not exist id")
    public void getTriangleAreaByNotExistId(){
        ErrorDto errorDto = TriangleController.getAreaByNotExistId(UUID.randomUUID().toString());
        assertThat(errorDto).extracting("status", "error", "exception", "message")
                .containsExactly(404, "Not Found", "com.natera.test.triangle.exception.NotFounException", "Not Found");
    }

    @Test(description = "Get empty list of triangles if no triangles were created")
    public void getEmptyTrianglesList(){
        List<TriangleDto> triangleDtos = TriangleController.getAll();
        assertThat(triangleDtos).isEmpty();
    }

    @Test(description = "Get full list of available triangles")
    public void getFullTrianglesList(){
        List<TriangleDto> expectedTriangleDtos = new ArrayList<>();
        TriangleReqDto triangleReqDto = TriangleReqDto.builder().input("2;3;4").build();
        for (int i=0; i<10;i++) {
            TriangleDto triangleDto = TriangleController.create(triangleReqDto);
            expectedTriangleDtos.add(triangleDto);
        }
        List<TriangleDto> actualTriangleDtos = TriangleController.getAll();
        assertThat(actualTriangleDtos).containsExactlyInAnyOrderElementsOf(expectedTriangleDtos);
    }
}

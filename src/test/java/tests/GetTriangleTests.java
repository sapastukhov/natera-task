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

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Triangle getting GET /triangle")
public class GetTriangleTests extends Fixture {

    @DataProvider(name = "Area Values")
    public static Object[][] areaValues() {
        return new Object[][]{
                {"5.0;7.0;7.0",16.345871},
                {"7;7;7", 21.217623},
                {"2e1;3e1;4e1", 290.47375},
                {"4294967295;1;4294967295", 2.14748365E9},
        };
    }

    @DataProvider(name = "Perimeter Values")
    public static Object[][] perimeterValues() {
        return new Object[][]{
                {"5.0;7.0;7.0",19.0},
                {"7;7;7", 21.0},
                {"2e1;3e1;4e1", 90.0},
                {"4294967295;1;4294967295", 8.5899346E9},
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

    @Test(description = "Not found error when try to get triangle by incorrect id")
    public void getTriangleByIncorrectId(){
        ErrorDto errorDto = TriangleController.getByIncorrectId("1");
        assertThat(errorDto).extracting("status", "error", "exception", "message")
                .containsExactly(404, "Not Found", "com.natera.test.triangle.exception.NotFounException", "Not Found");
    }

    @Test(description = "Not found error when try to get triangle perimeter by incorrect id")
    public void getTrianglePerimeterByIncorrectId(){
        ErrorDto errorDto = TriangleController.getPerimeterByIncorrectId("1");
        assertThat(errorDto).extracting("status", "error", "exception", "message")
                .containsExactly(404, "Not Found", "com.natera.test.triangle.exception.NotFounException", "Not Found");
    }

    @Test(description = "Not found error when try to get triangle area by incorrect id")
    public void getTriangleAreaByIncorrectId(){
        ErrorDto errorDto = TriangleController.getAreaByIncorrectId("1");
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

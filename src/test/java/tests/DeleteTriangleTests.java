package tests;

import controllers.TriangleController;
import fixtures.Fixture;
import io.qameta.allure.Feature;
import models.ErrorDto;
import models.TriangleDto;
import models.TriangleReqDto;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Triangle deleteting DELETE /triangle")
public class DeleteTriangleTests extends Fixture {
    @Test(description = "Delete triangle by existing id")
    public void deleteTriangleByExistingId(){
        TriangleReqDto triangleReqDto = TriangleReqDto.builder().input("2;3;4").build();
        TriangleDto triangleDto = TriangleController.create(triangleReqDto);
        TriangleController.deleteById(triangleDto.getId());
        TriangleController.getByNotExistId(triangleDto.getId());
    }

    @Test(description = "Bad Request error when try to delete triangle by incorrect id")
    public void deleteTriangleByIncorrectId(){
        TriangleController.deleteByIncorrectId("1");
    }

    @Test(description = "Not found error when try to delete triangle by not exist id")
    public void deleteTriangleByNotExistId(){
        ErrorDto errorDto = TriangleController.deleteByNotExistId(UUID.randomUUID().toString());
        assertThat(errorDto).extracting("status", "error", "exception", "message")
                .containsExactly(404, "Not Found", "com.natera.test.triangle.exception.NotFounException", "Not Found");
    }
}

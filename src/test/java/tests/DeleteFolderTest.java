package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteFolderTest extends BaseTest {

    @Test
    @DisplayName("Удаление папки - успешный сценарий")
    @Description("Проверяет, что папка успешно удаляется")
    @Severity(SeverityLevel.CRITICAL)
    void testDeleteFolderSuccess() {
        log.info("Тест: удаление папки {}", TEST_FOLDER);

        // 1. Создаём папку
        Response createResponse = client.createFolder(TEST_FOLDER);
        assertEquals(201, createResponse.statusCode(), "Папка должна создаться");

        // 2. Удаляем папку
        Response deleteResponse = client.deleteResource(TEST_FOLDER);
        assertEquals(204, deleteResponse.statusCode(), "Удаление должно вернуть 204 No Content");

        // 3. Проверяем, что папки больше нет
        Response getResponse = client.getFolderInfo(TEST_FOLDER);
        assertEquals(404, getResponse.statusCode(), "Папка должна отсутствовать (404)");

        log.info("Папка {} успешно удалена", TEST_FOLDER);
    }

    @Test
    @DisplayName("Удаление несуществующей папки")
    @Description("Проверяет, что при удалении несуществующей папки возвращается ошибка 404")
    @Severity(SeverityLevel.NORMAL)
    void testDeleteNonExistentFolder() {
        log.info("Тест: удаление несуществующей папки");

        String nonExistentFolder = "non-existent-folder-" + System.currentTimeMillis();

        Response response = client.deleteResource(nonExistentFolder);
        assertEquals(404, response.statusCode(), "Должна быть ошибка 404 Not Found");

        log.info("Удаление несуществующей папки вернуло 404, как и ожидалось");
    }

    @Test
    @DisplayName("Удаление с пустым именем")
    @Description("Проверяет, что при удалении с пустым именем выбрасывается исключение")
    @Severity(SeverityLevel.MINOR)
    void testDeleteWithEmptyName() {
        log.info("Тест: удаление с пустым именем");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> client.deleteResource("")
        );

        assertTrue(exception.getMessage().contains("не может быть пустым"));
        log.info("Исключение выброшено корректно");
    }

    @Test
    @DisplayName("Удаление с null именем")
    @Description("Проверяет, что при удалении с null выбрасывается исключение")
    @Severity(SeverityLevel.MINOR)
    void testDeleteWithNullName() {
        log.info("Тест: удаление с null именем");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> client.deleteResource(null)
        );

        assertTrue(exception.getMessage().contains("не может быть пустым"));
        log.info("Исключение выброшено корректно");
    }
}
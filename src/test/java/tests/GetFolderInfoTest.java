package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetFolderInfoTest extends BaseTest {

    @Test
    @DisplayName("Получение информации о существующей папке")
    @Description("Проверяет, что API возвращает корректную информацию о папке")
    @Severity(SeverityLevel.NORMAL)
    void testGetExistingFolder() {
        log.info("Тест: получение информации о существующей папке");

        // 1. Создаём папку
        client.createFolder(TEST_FOLDER);

        // 2. Получаем информацию
        Response response = client.getFolderInfo(TEST_FOLDER);

        // 3. Проверяем ответ
        assertEquals(200, response.statusCode(), "Должен быть статус 200");
        assertNotNull(response.jsonPath().getString("name"), "Имя папки не должно быть null");
        assertEquals(TEST_FOLDER, response.jsonPath().getString("name"), "Имя папки не совпадает");
        assertNotNull(response.jsonPath().getString("created"), "Дата создания не должна быть null");
        assertEquals("dir", response.jsonPath().getString("type"), "Тип должен быть 'dir'");

        log.info("Информация о папке получена корректно");
    }

    @Test
    @DisplayName("Получение информации о несуществующей папке")
    @Description("Проверяет, что при запросе несуществующей папки возвращается 404")
    @Severity(SeverityLevel.NORMAL)
    void testGetNonExistentFolder() {
        log.info("Тест: получение информации о несуществующей папке");

        String nonExistentFolder = "non-existent-folder-" + System.currentTimeMillis();

        Response response = client.getFolderInfo(nonExistentFolder);
        assertEquals(404, response.statusCode(), "Должна быть ошибка 404");

        // Проверяем, что сообщение об ошибке не пустое
        String errorMessage = response.jsonPath().getString("message");
        assertNotNull(errorMessage, "Сообщение об ошибке не должно быть null");
        assertFalse(errorMessage.isEmpty(), "Сообщение об ошибке не должно быть пустым");

        log.info("Запрос несуществующей папки вернул 404 с сообщением: {}", errorMessage);
    }

    @Test
    @DisplayName("Получение информации с пустым именем")
    @Description("Проверяет, что при пустом имени выбрасывается исключение")
    @Severity(SeverityLevel.MINOR)
    void testGetWithEmptyName() {
        log.info("Тест: получение информации с пустым именем");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> client.getFolderInfo("")
        );

        assertTrue(exception.getMessage().contains("не может быть пустым"));
        log.info("Исключение выброшено корректно");
    }
}
package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateFolderTest extends BaseTest {

    @Test
    @DisplayName("Создание папки - успешный сценарий")
    @Description("Проверяет, что папка успешно создаётся на Яндекс.Диске")
    @Severity(SeverityLevel.CRITICAL)
    void testCreateFolderSuccess() {
        log.info("Тест: создание папки {}", TEST_FOLDER);

        // Создаём папку
        Response response = client.createFolder(TEST_FOLDER);

        // Проверяем статус код
        assertEquals(201, response.statusCode(), "Папка должна создаться с кодом 201");

        // Проверяем, что папка действительно существует
        Response getResponse = client.getFolderInfo(TEST_FOLDER);
        assertEquals(200, getResponse.statusCode(), "Папка должна существовать");
        assertNotNull(getResponse.jsonPath().getString("name"), "Имя папки не должно быть null");
        assertEquals(TEST_FOLDER, getResponse.jsonPath().getString("name"), "Имя папки не совпадает");

        log.info("Папка {} успешно создана и проверена", TEST_FOLDER);
    }

    @Test
    @DisplayName("Создание папки - пустое имя")
    @Description("Проверяет, что при попытке создать папку с пустым именем выбрасывается исключение")
    @Severity(SeverityLevel.NORMAL)
    void testCreateFolderWithEmptyName() {
        log.info("Тест: создание папки с пустым именем");

        // Проверяем, что метод выбрасывает исключение
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> client.createFolder("")
        );

        assertTrue(exception.getMessage().contains("не может быть пустым"));
        log.info("Исключение выброшено корректно: {}", exception.getMessage());
    }

    @Test
    @DisplayName("Создание папки - null имя")
    @Description("Проверяет, что при попытке создать папку с null выбрасывается исключение")
    @Severity(SeverityLevel.NORMAL)
    void testCreateFolderWithNullName() {
        log.info("Тест: создание папки с null именем");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> client.createFolder(null)
        );

        assertTrue(exception.getMessage().contains("не может быть пустым"));
        log.info("Исключение выброшено корректно");
    }

    @Test
    @DisplayName("Создание существующей папки")
    @Description("Проверяет, что при попытке создать уже существующую папку возвращается ошибка 409")
    @Severity(SeverityLevel.NORMAL)
    void testCreateExistingFolder() {
        log.info("Тест: создание существующей папки");

        // Создаём папку первый раз
        Response firstResponse = client.createFolder(TEST_FOLDER);
        assertEquals(201, firstResponse.statusCode(), "Папка должна создаться");

        // Пытаемся создать ту же папку повторно
        Response secondResponse = client.createFolder(TEST_FOLDER);
        assertEquals(409, secondResponse.statusCode(), "Должна быть ошибка 409 Conflict");

        log.info("Повторное создание папки вернуло 409, как и ожидалось");
    }
}
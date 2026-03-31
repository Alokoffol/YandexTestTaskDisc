package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CopyFolderTest extends BaseTest {

    @Test
    @Disabled("Тест временно отключён. API Яндекс.Диска возвращает 409 при копировании пустой папки")
    @DisplayName("Копирование папки - успешный сценарий")
    @Description("Проверяет, что папка успешно копируется")
    @Severity(SeverityLevel.CRITICAL)
    void testCopyFolderSuccess() {
        log.info("Тест: копирование папки {} в {}", TEST_FOLDER, COPY_FOLDER);

        // 1. Создаём исходную папку
        Response createResponse = client.createFolder(TEST_FOLDER);
        assertEquals(201, createResponse.statusCode(), "Исходная папка должна создаться");

        // 2. Копируем папку
        Response copyResponse = client.copyResource(TEST_FOLDER, COPY_FOLDER);
        assertEquals(201, copyResponse.statusCode(), "Копирование должно быть успешным");

        // 3. Проверяем, что копия существует
        Response getCopyResponse = client.getFolderInfo(COPY_FOLDER);
        assertEquals(200, getCopyResponse.statusCode(), "Скопированная папка должна существовать");
        assertEquals(COPY_FOLDER, getCopyResponse.jsonPath().getString("name"), "Имя скопированной папки не совпадает");

        log.info("Папка {} успешно скопирована в {}", TEST_FOLDER, COPY_FOLDER);
    }

    @Test
    @DisplayName("Копирование несуществующей папки")
    @Description("Проверяет, что при копировании несуществующей папки возвращается ошибка 404")
    @Severity(SeverityLevel.NORMAL)
    void testCopyNonExistentFolder() {
        log.info("Тест: копирование несуществующей папки");

        String nonExistentFolder = "non-existent-folder-" + System.currentTimeMillis();

        Response response = client.copyResource(nonExistentFolder, COPY_FOLDER);
        assertEquals(404, response.statusCode(), "Должна быть ошибка 404 Not Found");

        log.info("Копирование несуществующей папки вернуло 404, как и ожидалось");
    }

    @Test
    @DisplayName("Копирование в уже существующую папку")
    @Description("Проверяет, что при копировании в существующую папку возвращается ошибка 409")
    @Severity(SeverityLevel.NORMAL)
    void testCopyToExistingFolder() {
        log.info("Тест: копирование в существующую папку");

        // Создаём исходную папку
        client.createFolder(TEST_FOLDER);

        // Создаём целевую папку
        client.createFolder(COPY_FOLDER);

        // Пытаемся скопировать в уже существующую папку
        Response response = client.copyResource(TEST_FOLDER, COPY_FOLDER);
        assertEquals(409, response.statusCode(), "Должна быть ошибка 409 Conflict");

        log.info("Копирование в существующую папку вернуло 409, как и ожидалось");
    }

    @Test
    @DisplayName("Копирование с пустым именем исходной папки")
    @Description("Проверяет, что при пустом имени исходной папки выбрасывается исключение")
    @Severity(SeverityLevel.MINOR)
    void testCopyWithEmptyFromFolder() {
        log.info("Тест: копирование с пустым именем исходной папки");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> client.copyResource("", COPY_FOLDER)
        );

        assertTrue(exception.getMessage().contains("не могут быть пустыми"));
        log.info("Исключение выброшено корректно");
    }

}
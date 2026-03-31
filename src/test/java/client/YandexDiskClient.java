package client;

import config.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;



public class YandexDiskClient {
    private static final Logger log = LoggerFactory.getLogger(YandexDiskClient.class);
    private final RequestSpecification spec;

    public YandexDiskClient() {
        this.spec = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "OAuth " + Config.ACCESS_TOKEN)
                .contentType(ContentType.JSON)
                .log().ifValidationFails();
    }

    public Response createFolder(String folderName) {
        if (folderName == null || folderName.isEmpty()) {
            log.error("Имя папки пустое или содержит значение null.");
            throw new IllegalArgumentException("Имя папки не может быть пустым.");
        }

        String fullPath = "app:/" + folderName;
        log.info("Создание папки: {}", fullPath);

        return spec
                .queryParam("path", fullPath)
                .put(Config.BASE_URL + "/resources")
                .then()
                .extract()
                .response();
    }

    public Response getFolderInfo(String folderName) {
        if (folderName == null || folderName.isEmpty()) {
            log.error("Имя папки пустое или содержит значение null.");
            throw new IllegalArgumentException("Имя папки не может быть пустым.");
        }

        String fullPath = "app:/" + folderName;
        log.info("Получение информации о папке: {}", fullPath);

        return spec
                .queryParam("path", fullPath)
                .get(Config.BASE_URL + "/resources")
                .then()
                .extract()
                .response();
    }

    public Response deleteResource(String folderName) {
        if (folderName == null || folderName.isEmpty()) {
            log.error("Имя папки пустое или содержит значение null.");
            throw new IllegalArgumentException("Имя папки не может быть пустым.");
        }

        String fullPath = "app:/" + folderName;
        log.info("Удаление папки: {} (окончательно)", fullPath);

        return spec
                .queryParam("path", fullPath)
                .queryParam("permanently", true)
                .delete(Config.BASE_URL + "/resources")
                .then()
                .extract()
                .response();
    }

    public Response copyResource(String fromFolder, String toFolder) {
        if (fromFolder == null || fromFolder.isEmpty() || toFolder == null || toFolder.isEmpty()) {
            log.error("Имя исходной или целевой папки пустое или содержит значение null.");
            throw new IllegalArgumentException("Имена папок не могут быть пустыми.");
        }

        String fullPathFrom = "app:/" + fromFolder;
        String fullPathTo = "app:/" + toFolder;

        log.info("Копирование папки: {} -> {}", fullPathFrom, fullPathTo);

        return spec
                .queryParam("from", fullPathFrom)
                .queryParam("path", fullPathTo)
                .post(Config.BASE_URL + "/resources/copy")
                .then()
                .extract()
                .response();
    }

}
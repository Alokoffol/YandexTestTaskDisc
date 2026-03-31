package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    public static final String BASE_URL = "https://cloud-api.yandex.net/v1/disk";
    public static final String ACCESS_TOKEN;
    public static final String APP_PATH = "app:/";

    static {
        // Получаем токен из переменной окружения или системного свойства
        log.info("Получение токена");
        String token = System.getenv("YANDEX_DISK_TOKEN");

        if (token != null && !token.isEmpty()) {
            log.info("Токен получен из переменной окружения");
        } else {
            token = System.getProperty("yandex.token");
            if (token != null && !token.isEmpty()) {
                log.info("Токен получен из системного свойства");
            }
        }

        if (token == null || token.isEmpty()) {
            log.error("YANDEX_DISK_TOKEN Не найден!");
            throw new IllegalStateException("Требуется YANDEX_DISK_TOKEN");
        }

        ACCESS_TOKEN = token;
        log.info("Токен успешно прочитан");
    }
}
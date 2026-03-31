package base;

import client.YandexDiskClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {
    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected YandexDiskClient client;
    protected static final String TEST_FOLDER = "test-folder";
    protected static final String COPY_FOLDER = "test-folder-copy";

    @BeforeEach
    public void setUp() {
        log.info("========== Начало теста ==========");
        client = new YandexDiskClient();
        log.info("Клиент инициализирован");
    }

    @AfterEach
    public void cleanUp() {
        log.info("========== Очистка после теста ==========");
        try {
            // Удаляем тестовые папки после каждого теста
            client.deleteResource(TEST_FOLDER);
            client.deleteResource(COPY_FOLDER);
            log.info("Очистка завершена");
        } catch (Exception e) {
            log.warn("Ошибка при очистке: {}", e.getMessage());
        }
    }
}
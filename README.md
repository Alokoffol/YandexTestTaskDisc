# Yandex.Disk API Auto-Tests

[![Java CI](https://github.com/yourusername/yandex-disk-tests/actions/workflows/test.yml/badge.svg)](https://github.com/yourusername/yandex-disk-tests/actions/workflows/test.yml)

Автотесты для REST API Яндекс.Диска. Проект выполнен в рамках тестового задания для стажера "Инженер по автоматизации тестирования в Финтех" (Яндекс).

## Содержание

- [Технологии](#технологии)
- [Структура проекта](#структура-проекта)
- [Что тестируется](#что-тестируется)
- [Установка и запуск](#установка-и-запуск)
- [Allure отчёт](#allure-отчёт)
- [GitHub Actions](#github-actions)
- [Результаты](#результаты)

## Технологии

| Технология | Версия | Назначение |
|------------|--------|------------|
| Java | 11 | Язык программирования |
| JUnit 5 | 5.10.0 | Фреймворк для тестирования |
| RestAssured | 5.4.0 | Тестирование REST API |
| Allure | 2.25.0 | Формирование отчётов |
| SLF4J + Logback | 2.0.9 / 1.4.11 | Логирование |
| Maven | 3.6+ | Сборка проекта |
| GitHub Actions | - | CI/CD |

## Структура проекта
```text
yandex-disk-tests/
├── .github/workflows/
│ └── test.yml # GitHub Actions CI/CD
├── src/test/java/
│ ├── base/
│ │ └── BaseTest.java # Базовый класс тестов
│ ├── client/
│ │ └── YandexDiskClient.java # Клиент для работы с API
│ ├── config/
│ │ └── Config.java # Конфигурация (токен, URL)
│ └── tests/
│ ├── CreateFolderTest.java # Тесты создания папки (PUT)
│ ├── GetFolderInfoTest.java # Тесты получения информации (GET)
│ ├── CopyFolderTest.java # Тесты копирования (POST)
│ └── DeleteFolderTest.java # Тесты удаления (DELETE)
├── .gitignore
├── pom.xml
└── README.md



## Что тестируется

### CRUD операции

| Метод | HTTP | Эндпоинт | Тест |
|-------|------|----------|------|
| Создание папки | PUT | `/resources` | `CreateFolderTest` |
| Получение информации | GET | `/resources` | `GetFolderInfoTest` |
| Копирование | POST | `/resources/copy` | `CopyFolderTest` |
| Удаление | DELETE | `/resources` | `DeleteFolderTest` |

### Сценарии тестирования

#### Позитивные сценарии 
- Создание новой папки
- Получение информации о существующей папке
- Копирование папки
- Удаление папки

#### Негативные сценарии 
- Создание уже существующей папки → `409 Conflict`
- Получение несуществующей папки → `404 Not Found`
- Копирование несуществующей папки → `404 Not Found`
- Копирование в уже существующую папку → `409 Conflict`
- Удаление несуществующей папки → `404 Not Found`

#### Валидация входных данных
- Пустое имя папки → `IllegalArgumentException`
- `null` имя папки → `IllegalArgumentException`

## Установка и запуск

## Требования

- Java 11 или выше
- Maven 3.6 или выше
- OAuth-токен Яндекс.Диска (тестовый)

### Получение OAuth-токена

1. Перейдите на [полигон Яндекс.Диска](https://yandex.ru/dev/disk/poligon/)
2. Нажмите "Получить токен"
3. Скопируйте полученный токен (начинается с `y0_`)

## Установка и запуск

### 1. Клонирование репозитория

```bash
git clone https://github.com/Alokoffol/YandexTestTaskDisc.git
cd YandexTestTaskDisc

# Установить ТОКЕН в переменную окружения
YANDEX_DISK_TOKEN и ваш токен

# Запустить тесты
mvn clean test

mvn allure:report
open target/site/allure-maven-plugin/index.html

Автор 
Alokoffol
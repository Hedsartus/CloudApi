# Running
1. ```mvn clean install -DskipTests``` собираем JAR-файл
2. Запускаем ```docker-compose up```

Получаем: 
- MySql по адресу http://localhost:3306/ 
- Фронт по адресу http://localhost:3000/ 
- Бэкенд по адрес http://localhost:8080/ 

Запуск бэкенда происходит немного позже (автоматически), так как нужно дождаться запсука и формирования контейнера БД.

- Структура БД формируется системой Liquibase, в таблицу `users` добавляется дефолтный пользователь User {email: user@mail.ru, password: user}


## Описание проекта

Задача — разработать REST-сервис. Сервис должен предоставить REST-интерфейс для загрузки файлов и вывода списка уже загруженных файлов пользователя. 

Все запросы к сервису должны быть авторизованы. Заранее подготовленное веб-приложение (FRONT) должно подключаться к разработанному сервису без доработок, 
а также использовать функционал FRONT для авторизации, загрузки и вывода списка файлов пользователя.

## Требования к приложению

- Сервис должен предоставлять REST-интерфейс для интеграции с FRONT.
- Сервис должен реализовывать все методы, описанные в [yaml-файле](./CloudServiceSpecification.yaml):
  1. Вывод списка файлов.
  2. Добавление файла.
  3. Удаление файла.
  4. Авторизация.
- Все настройки должны вычитываться из файла настроек (yml).
- Информация о пользователях сервиса (логины для авторизации) и данные должны храниться в базе данных.

## Требования к реализации

- Приложение разработано с использованием Spring Boot.
- Использован сборщик пакетов gradle/maven.
- Для запуска используется docker, docker-compose.
- Код покрыт unit-тестами с использованием mockito.
- Добавлены интеграционные тесты с использованием testcontainers.

## Шаги реализации

- Изучите протокол получения и отправки сообщений между FRONT и BACKEND.
- Нарисуйте схему приложений.
- Опишите архитектуру приложения, где хранятся настройки и большие файлы, структуры таблиц/коллекций базы данных.
- Создайте репозиторий проекта на Github.
- Напишите приложение с использованием Spring Boot.
- Протестируйте приложение с помощью curl/postman.
- Протестируйте приложение с FRONT.
- Напишите README.md к проекту.
- Отправьте на проверку.

## Авторизация приложения

FRONT-приложение использует header `auth-token`, в котором отправляет токен (ключ-строка) для идентификации пользователя на BACKEND.
Для получения токена нужно пройти авторизацию на BACKEND и отправить на метод /login логин и пароль. В случае успешной проверки в ответ BACKEND должен вернуть json-объект
с полем `auth-token` и значением токена. Все дальейшие запросы с FRONTEND, кроме метода /login, отправляются с этим header.
Для выхода из приложения нужно вызвать метод BACKEND /logout, который удалит/деактивирует токен. Последующие запросы с этим токеном будут не авторизованы и вернут код 401.

Обратите внимание, что название самого параметра (как и всех параметров в спецификации), его регистр имеют значение. 
Важно, чтобы ваш backend возвращал токен в поле `auth-token` – если поле будет называться `authToken` или `authtoken`, фронт не сможет найти токен и дальше логина процесс не пройдёт.

## Дополнительные рекомендации


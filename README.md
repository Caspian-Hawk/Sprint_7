# Тестирование API учебного сервиса [Яндекс.Самокат](https://qa-scooter.praktikum-services.ru/)

Тесты написаны по следующим "ручкам":
* Создание курьера POST-запрос __/api/v1/courier__
* Логин курьера POST-запрос __/api/v1/courier/login__
* Создание заказа POST-запрос __/api/v1/orders__
* Список заказов GET-запрос __/api/v1/orders__

В проекте используется:
* JAVA 11
* JUnit 4
* RestAssured 5.5.1
* Allure 2.10.0

Запуск тестов: `mvn clean test`

Результаты тестов: `mvn allure:serve`
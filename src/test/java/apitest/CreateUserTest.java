package apitest;

import common.AuthUser;
import common.ClientUser;
import praktikum.api.RequestCreateUser;
import constant.Constants;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreateUserTest
{

    AuthUser userAuth;
    ClientUser responseUser;

    @Before
    @Step("Предусловия для создания тестов")
    public void setUp()
    {
        RestAssured.baseURI = Constants.BASE_URL;
        userAuth = AuthUser.userRandomCreate();
    }


    @Test
    @DisplayName("Создание нового пользователя")
    @Description("Тест на успешное создание пользователя с кодом состояния 200 ОК")
    public void userCanBeCreatedWithSuccess()
    {
        Response response = createUser(userAuth);
        assertEquals("Правильный код ответа", 200, response.statusCode());
        responseUser = response.body().as(ClientUser.class);
        assertTrue("В ответе сообщение об успехе", responseUser.isSuccess());
    }

    @Test
    @DisplayName("Cоздание пользователя с существующими данными")
    @Description("Проверка создания нового пользователя с существующим логином")
    public void userCanNotBeCreatedWithTheSameData() {
        Response response = createUser(userAuth);
        assertEquals("Правильный код ответа", 200, response.statusCode());
        responseUser = response.body().as(ClientUser.class);
        response = createUser(userAuth);
        ClientUser failRespUser = response.body().as(ClientUser.class);
        assertEquals("Неверный код ответа", 403, response.statusCode());
        assertFalse("Такой пользователь уже существует", failRespUser.isSuccess());
    }
    @Test
    @DisplayName("Создать пользователя без ввода имени")
    @Description("Проверка создания нового пользователя без заполнения поля имя")
    public void userCanNotBeCreatedWithoutNameField() {
        userAuth.setName(null);
        creationUserFailedField();
    }

    @Test
    @DisplayName("Создать пользователя без ввода пароля")
    @Description("Проверка создания нового пользователя без заполнения поля пароль")
    public void userCanNotBeCreatedWithoutPasswordField() {
        userAuth.setName(null);
        creationUserFailedField();
    }
    @Test
    @DisplayName("Создать пользователя без поля электронной почты")
    @Description("Проверка создания нового пользователя без поля электронной почты")
    public void userCanNotBeCreatedWithoutEmailField() {
        userAuth.setEmail(null);
        creationUserFailedField();
    }
    private void creationUserFailedField()
    {
        Response response = createUser(userAuth);
        responseUser = response.body().as(ClientUser.class);
        assertEquals("Неверный код ответа", 403, response.statusCode());
        assertFalse("Не заполнено обязательное поле", responseUser.isSuccess());
    }

    private Response createUser(AuthUser userAuth)
    {
        return RequestCreateUser.createUser(userAuth);
    }
    @After
    @Step("Удаление созданного пользователя")
    public void tearDown() {
        if (responseUser.getAccessToken() == null) {
            return;
        }
        RequestCreateUser.deleteUser(responseUser.getAccessToken());
    }
}

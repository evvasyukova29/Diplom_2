package apitest;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import common.AuthUser;
import common.User;
import praktikum.api.RequestCreateUser;
import constant.Constants;

public class UserTest
{
    AuthUser authUser;
    User responseUser;

    @Before
    @Step("Предусловия для создания тестов")
    public void setUp()
    {
        RestAssured.baseURI = Constants.BASE_URL;
        authUser = AuthUser.userRandomCreate();
    }

    public void creationUserFailedField()
    {
        Response response = createUser(authUser);
        responseUser = response.body().as(User.class);
        assertEquals("Неверный код ответа", 403, response.statusCode());
        assertFalse("Не заполнено обязательное поле", responseUser.isSuccess());
    }
    @Step("Отправка данных для создания нового пользователя")
    public Response createUser(AuthUser authUser)
    {
        return RequestCreateUser.createUser(authUser);
    }

    //-------Создание нового пользователя--------
    @Test
    @DisplayName("Создать нового пользователя")
    @Description("Успешное создание нового пользователя")
    public void userCanBeCreatedWithSuccess()
    {
        Response response = createUser(authUser);
        assertEquals("Правильный код ответа", 200, response.statusCode());
        responseUser = response.body().as(User.class);
        assertTrue("В ответе сообщение об успехе", responseUser.isSuccess());
    }

    //------------Создание нового пользователя с уже существующими данными---------
    @Test
    @DisplayName("Создать нового пользователя с уже существующими данными")
    @Description("Проверка на уникальности пользователя при создании")
    public void userCanNotBeCreatedWithTheSameData()
    {
        Response response = RequestCreateUser.createUser(authUser);
        assertEquals("Правильный код ответа", 200, response.statusCode());
        responseUser = response.body().as(User.class);
        response = createUser(authUser);
        User failRespUser = response.body().as(User.class);
        assertEquals("Неверный код ответа", 403, response.statusCode());
        assertFalse("Такой пользователь уже существует", failRespUser.isSuccess());
    }


    //-----Создание нового пользователя без имени-------
    @Test
    @DisplayName("Создать нового пользователя без имени")
    @Description("Проверка на создание нового пользователя без заполнения имени")
    public void userCanNotBeCreatedWithoutNameField() {
        authUser.setName(null);
        creationUserFailedField();
    }

    //-----Создание нового пользователя без пароля-------
    @Test
    @DisplayName("Создать нового пользователя без пароля")
    @Description("Проверка на создание нового пользователя без заполнения пароля")
    public void userCanNotBeCreatedWithoutPasswordField() {
        authUser.setPassword(null);
        creationUserFailedField();
    }

    //-----Создание нового пользователя без емайл-------
    @Test
    @DisplayName("Создать нового пользователя без емайл")
    @Description("Проверка на создание нового пользователя без заполнения емайл")
    public void userCanNotBeCreatedWithoutEmailField() {
        authUser.setEmail(null);
        creationUserFailedField();
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

package apitest;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

import constant.Constants;
import common.AuthUser;
import common.ClientUser;
import praktikum.api.RequestCreateUser;
public class UserAuthTest
{
    AuthUser userAuth;
    ClientUser userLog;

    @Before
    @Step("Предусловия для создания тестов")
    public void setUp()
    {
        RestAssured.baseURI = Constants.BASE_URL;
        userAuth = AuthUser.userRandomCreate();
        userLog = RequestCreateUser.createUser(userAuth).body().as(ClientUser.class);
    }

    @Test
    @DisplayName("Авторизация с корректными данными пользователя")
    @Description("Проверка успешной авторизации")
    public void userSuccessLogin() {
        Response response = RequestCreateUser.authUser(userAuth);
        assertTrue("Успешная авторизация", response.path("success"));
    }

    @Test
    @DisplayName("Авторизация без заполнения пароля")
    @Description("Проверка не успешной авторизации")
    public void userErrorLoginWithoutPassword() {
        userAuth.setPassword("qazwsxhjhk");
        Response response = RequestCreateUser.authUser(userAuth);
        assertFalse("Авторизоваться не удалось", response.path("success"));
    }

    @Test
    @DisplayName("Авторизация без заполнения логина")
    @Description("Проверка не успешной авторизации")
    public void userErrorLoginWithoutLogin() {
        userAuth.setEmail(RandomStringUtils.randomAlphabetic(12) + "@yandex.ru");
        Response response = RequestCreateUser.authUser(userAuth);
        assertFalse("Авторизоваться не удалось", response.path("success"));
    }

    @After
    public void tearDown() {
        RequestCreateUser.deleteUser(userLog.getAccessToken());
    }

}

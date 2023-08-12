package apitest;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

import constant.Constants;
import common.AuthUser;
import common.ClientUser;
import praktikum.api.RequestCreateUser;

public class UserAuthChangingTest
{
    AuthUser userAuth;
    ClientUser userUpdate;

    @Before
    @Step("Предусловия для создания тестов")
    public void setUp()
    {
        RestAssured.baseURI = Constants.BASE_URL;
        userAuth = AuthUser.userRandomCreate();
        userUpdate = RequestCreateUser.createUser(userAuth).body().as(ClientUser.class);
    }

    @Test
    @DisplayName("Изменение почты и имени авторизованного пользователя")
    @Description("Проверка возможности авторизации после изменения данных пользователя")
    public void checkAuthNewUser()
    {
        AuthUser newUserDataAuthorizationData = AuthUser.userRandomCreate();
        newUserDataAuthorizationData.setPassword("qwerty991");
        Response response = RequestCreateUser.updateUserData(newUserDataAuthorizationData, userUpdate.getAccessToken());
        assertTrue("Ответ успешный со статутом 200 ОК", response.path("success"));
        assertTrue("С новыми данными можно залогиниться", RequestCreateUser.authUser(newUserDataAuthorizationData).path("success"));
    }

    @Test
    @DisplayName("Изменение почты и имени неавторизованного пользователя")
    @Description("Проверить невозможности изменения данных у неавторизованного пользователя")
    public void checkAuthUserCouldNotChange() {
        AuthUser newUserDataAuthorizationData = AuthUser.userRandomCreate();
        newUserDataAuthorizationData.setPassword("qwerty9911");
        Response response = given()
                .header("Content-Type", "application/json")
                .body(userAuth).patch(Constants.USER_AUTH);
        assertFalse("В ответе сообщение о неудачной авторизации", response.path("success"));
        assertEquals("Ошибка авторизации. Код 401", 401, response.statusCode());
    }

    @After
    @Step("Удаление созданного пользователя")
    public void tearDown() {
        RequestCreateUser.deleteUser(userUpdate.getAccessToken());
    }

}

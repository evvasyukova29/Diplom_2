package apitest;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;
import static praktikum.api.RequestIngredient.getFirstIngredientOnTheList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import constant.Constants;
import common.Ingredients;
import common.Order;
import common.AuthUser;
import praktikum.api.RequestCreateUser;
import praktikum.api.RequestOrder;

public class OrderTest
{
    Ingredients currentListIngredient;
    String authToken;

    @Before
    @Step("Предусловия для создания тестов")
    public void setUp()
    {
        RestAssured.baseURI = Constants.BASE_URL;
        authToken = RequestCreateUser.createUser(AuthUser.userRandomCreate()).path("accessToken");
        currentListIngredient = getFirstIngredientOnTheList();
    }

    //-------Новый заказ из авторизованный зоны--------
    @Test
    @DisplayName("Создание нового заказа авторизованным клиентом")
    @Description("Успешное создание нового заказа авторизованным клиентом (200 ОК)")
    public void createNewOrderByAuth() {
        Order order = new Order(currentListIngredient);
        Response response = RequestOrder.createOrder(order, authToken);
        assertTrue("Ответ успешный StatusCode = 200 ОК", response.path("success"));
    }

    //--------Новый заказ из неавторизованной зоны----------
    @Test
    @DisplayName("Создание нового заказа неавторизованным клиентом")
    @Description("Успешное создание нового заказа неавторизованным клиентом (200 ОК)\"")
    public void createNewOrderWithoutAuth() {
        Order order = new Order(currentListIngredient);
        Response response = given()
                .header("Content-Type", "application/json")
                .body(order)
                .post(Constants.ORDERS_BURGER);
        assertTrue("Ответ успешный со статутом 200 ОК", response.path("success"));
    }


    //---------Новый заказ из авторизованной зоны без ингредиентов-----
    @Test
    @DisplayName("Создание нового заказа из авторизованной зоны без выбора игредиентов")
    @Description("Успешное создание заказа без игредиентов (с авторизацией)")
    public void createNewOrderAuthUserNoIngredients() {
        Order order = new Order();
        Response response = RequestOrder.createOrder(order, authToken);
        assertFalse("Заказ не создан", response.path("success"));
        assertEquals("Ответ со статусом Bad Request 400", 400, response.statusCode());
    }

    //-----------Новый заказ из неавторизованной зоны без ингредиентов-------
    @Test
    @DisplayName("Создание нового заказа из неавторизованной зоны без выбора игредиентов")
    @Description("Успешное создание заказа без игредиентов (без авторизации)")
    public void createNewOrderWithoutAuthUserIngredientsInvalidHash() {
        currentListIngredient.set_id(currentListIngredient.get_id().replace("a", "0"));
        Order order = new Order(currentListIngredient);
        Response response = RequestOrder.createOrder(order, authToken);
        assertFalse("Заказ не создан", response.path("success"));
        assertEquals("Ответ со статусом Bad Request 400", 400, response.statusCode());
    }

    //---------Новый заказ из авторизованный зоны с неправильным набором ингредиентов-------
    @Test
    @DisplayName("Создание нового заказа с неправильными ингредиентами")
    @Description("Заказ не создан")
    public void createNewOrderAuthUserWithWrongIngredients() {
        currentListIngredient.set_id("Неправильный ингредиент");
        Order order = new Order(currentListIngredient);
        Response response = RequestOrder.createOrder(order, authToken);
        assertEquals("Ответ Internal Error со статусом 500", 500, response.statusCode());
    }

    @After
    @Step("Удалить тестового пользователя")
    public void tearDown() {
        RequestCreateUser.deleteUser(authToken);
    }

}

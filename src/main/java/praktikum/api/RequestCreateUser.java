package praktikum.api;
import common.AuthUser;
import constant.Constants;
import io.restassured.response.Response;
import common.AuthUser;

import static io.restassured.RestAssured.given;

public class RequestCreateUser {

    public static Response createUser(AuthUser authUser)
    {
        return given()
                .header("Content-Type", "application/json")
                .body(authUser)
                .post(Constants.CREATE_NEW_USER);
    }

    public static Response authUser(AuthUser authUser)
    {
        return given()
                .header("Content-Type", "application/json")
                .body(authUser)
                .post(Constants.LOGIN_USER);
    }

    public static Response updateUserData(AuthUser authUser, String authToken)
    {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .body(authUser)
                .patch(Constants.USER_AUTH);
    }

    public static Response deleteUser(String authToken)
    {
        String AuthRoute = Constants.USER_AUTH;
        return given()
                .header("Authorization", authToken)
                .delete(AuthRoute);
    }
}

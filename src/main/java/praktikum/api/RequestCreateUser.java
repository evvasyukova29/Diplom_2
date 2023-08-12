package praktikum.api;
import common.AuthUser;
import common.ClientUser;
import constant.Constants;
import io.restassured.response.Response;
import common.AuthUser;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class RequestCreateUser
{

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

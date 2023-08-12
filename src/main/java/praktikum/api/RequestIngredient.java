package praktikum.api;
import constant.Constants;
import common.Ingredients;
import common.IngredientsList;

import static io.restassured.RestAssured.given;
public class RequestIngredient
{
        public static Ingredients[] getIngredientsArray() {
            return getIngredientResponse().getIngredients();
        }
        public static IngredientsList getIngredientResponse() {
            return given().get(Constants.GET_INGREDIENTS).as(IngredientsList.class);
        }
        public static Ingredients getFirstIngredientOnTheList() {
            return getIngredientsArray()[0];
        }
}

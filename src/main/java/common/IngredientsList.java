package common;

import common.Ingredients;

public class IngredientsList
{
    private boolean success;
    private Ingredients[] data;

    public boolean isSuccess()
    {
        return success;
    }
    public void setSuccess(boolean success)
    {
        this.success = success;
    }
    public Ingredients[] getIngredients()
    {
        return data;
    }
    public void setIngredients(Ingredients[] ingredients)
    {
        this.data = ingredients;
    }
}

package orderpojos;

import java.util.List;

public class OrderPojo {
    private List<String> ingredients;

    public OrderPojo(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}

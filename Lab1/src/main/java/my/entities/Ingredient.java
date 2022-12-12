package my.entities;

import java.util.Objects;

public class Ingredient {

    private final Vegetable ingredient;
    private double weight;//in kilograms

    public Ingredient(Vegetable veggie, double weight) {
        this.ingredient = veggie;
        if(weight < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        this.weight = weight;
    }

    //If amount not given, just set it to zero
    public Ingredient(Vegetable veggie) {
        this.ingredient = veggie;
        this.weight = 0;
    }

    public double getWeight() {
        return this.weight;
    }

    public Vegetable getIngredient() {
        return this.ingredient;
    }

   public double getCalories(){
        return weight * ingredient.getCaloriesPer100g() * 10;
   }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Double.compare(that.weight, weight) == 0 && ingredient == that.ingredient;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, weight);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredient=" + ingredient +
                ", weight=" + weight +
                ", caloric value=" + getCalories() +
                '}';
    }
}

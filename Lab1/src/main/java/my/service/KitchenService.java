package my.service;

import my.entities.Ingredient;

import java.util.Comparator;
import java.util.List;

public class KitchenService {

    List<Ingredient> ingredients;

    public KitchenService(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void printIngredients() {
        System.out.println("List of ingredients:");
        ingredients.forEach(System.out::println);
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public double totalCalories() {
        return ingredients.stream().mapToDouble(Ingredient::getCalories).sum();
    }

    public void sortByCaloricContent() {
        ingredients.sort(Comparator.comparing(Ingredient::getCalories));
    }

    public List<Ingredient> findByRange(double from, double to) {

        return ingredients.stream()
                .filter(ingredient -> ingredient.getCalories() >= from && ingredient.getCalories() <= to)
                .toList();
    }
}
package my;

import my.entities.Ingredient;
import my.service.KitchenService;

import java.util.ArrayList;
import java.util.List;

import static my.entities.Vegetable.*;

public class Salad {
    private List<Ingredient> ingredients;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void fillIn() {
        ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(POTATO, 2.5));
        ingredients.add(new Ingredient(TOMATO, 3.5));
        ingredients.add(new Ingredient(GARLIC, 1.5));
        ingredients.add(new Ingredient(CUCUMBER, 2));

    }

    public static void main(String[] args) {
        Salad salad = new Salad();
        salad.fillIn();
        KitchenService kitchenService = new KitchenService(salad.getIngredients());

        kitchenService.printIngredients();
        System.out.println("\nTotal calories: ");
        System.out.println(kitchenService.totalCalories());
        kitchenService.sortByCaloricContent();
        System.out.println("\nAfter sorting");
        kitchenService.printIngredients();
        System.out.println();
        System.out.println(kitchenService.findByRange(1, 500));
    }
}
package my.service;

import my.Salad;
import my.entities.Ingredient;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static my.entities.Vegetable.CUCUMBER;
import static org.junit.jupiter.api.Assertions.*;

class KitchenServiceTest {

    private Salad salad;
    private KitchenService kitchenService;
    @BeforeEach
    void setUp() {
        salad = new Salad();
        salad.fillIn();
        kitchenService = new KitchenService(salad.getIngredients());
    }

    @org.junit.jupiter.api.Test
    void totalCalories() {
        assertEquals(5040.0, kitchenService.totalCalories());
    }

    @org.junit.jupiter.api.Test
    void sortByCaloricContent() {
        List<Ingredient> expected = kitchenService.getIngredients();
                expected.sort(Comparator.comparing(Ingredient::getCalories));
        kitchenService.sortByCaloricContent();
        assertEquals(expected,
                kitchenService.getIngredients());
    }

    @org.junit.jupiter.api.Test
    void findByRange() {
        List<Ingredient> exp = new ArrayList<>();
        exp.add(new Ingredient(CUCUMBER, 2));
        assertEquals(exp, kitchenService.findByRange(1, 500));
    }


}
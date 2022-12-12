package my.entities;

public enum Vegetable {

    GARLIC(149),
    ONION(40),

    CABBAGE(25),
    BROCCOLI(34),
    CAULIFLOWER(25),
    CUCUMBER( 16),
    PUMPKIN(25),
    ZUCCHINI( 17),
    CARROT( 41),
    CELERY(16),
    RADISH( 16),
    POTATO( 77),
    SWEET_POTATO(86),
    CORN(86),
    PARSLEY(45),
    PEAS( 81),
    TOMATO(16);

    private final double caloriesPer100g;

    Vegetable(double caloriesPer100g) {
        this.caloriesPer100g = caloriesPer100g;
    }

    public double getCaloriesPer100g() {
        return this.caloriesPer100g;
    }

}
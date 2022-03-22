package bussiness;

import java.io.Serializable;
import java.util.Objects;

public class BaseProduct extends MenuItem implements Serializable {
    private String name;
    private float rating;
    private float calories;
    private float proteins;
    private float fats;
    private float sodium;
    private float price;

    public BaseProduct(String name, float rating, float calories, float proteins, float fats, float sodium, float price) {
        this.name = name;
        this.rating = rating;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.sodium = sodium;
        this.price = price;
    }
    public BaseProduct(String name, float[] values) {
        this.name = name;
        this.rating = values[0];
        this.calories = values[1];
        this.proteins = values[2];
        this.fats = values[3];
        this.sodium = values[4];
        this.price = values[5];
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getCalories() {
        return calories;
    }
    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getProteins() {
        return proteins;
    }
    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public float getFats() {
        return fats;
    }
    public void setFats(float fats) {
        this.fats = fats;
    }

    public float getSodium() {
        return sodium;
    }
    public void setSodium(float sodium) {
        this.sodium = sodium;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
    @Override
    public float computePrice() {
        return price;
    }
}

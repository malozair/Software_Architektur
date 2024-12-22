package de.hs.swa.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mocktail {
    private String id;
    private List<Ingredient> ingredient = new ArrayList<>();
    private String name;

    public Mocktail() {}

    public Mocktail(String id, List<Ingredient> ingredient, String name) {
        this.id = id;
        this.ingredient = ingredient;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Ingredient> getIngredient() {
        return this.ingredient;
    }

    public void setIngredient(List<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mocktail mocktail = (Mocktail) o;
        return Objects.equals(id, mocktail.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Mocktail{" +
                "id='" + id + '\'' +
                ", ingredient=" + ingredient +
                ", name='" + name + '\'' +
                '}';
    }
}

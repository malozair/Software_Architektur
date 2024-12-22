package de.hs.swa.boundary;

import jakarta.json.bind.annotation.JsonbProperty;

import java.util.List;

public class DrinkDTO {
    @JsonbProperty("idDrink")
    private String id;
    @JsonbProperty("strDrink")
    private String name;
    @JsonbProperty("strCategory")
    private String category;
    private List<String> ingredients;

    public DrinkDTO() {}

    public DrinkDTO(String id, String name, String category, List<String> ingredients) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
}
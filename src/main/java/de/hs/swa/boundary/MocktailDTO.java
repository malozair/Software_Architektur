package de.hs.swa.boundary;

import jakarta.json.bind.annotation.JsonbProperty;

import java.util.List;
import java.util.Objects;

public class MocktailDTO {
    @JsonbProperty("id")
    private String id;

    @JsonbProperty("name")
    private String name;

    @JsonbProperty("ingredients")
    private List<IngredientDTO> ingredients;

    public MocktailDTO() {
    }

    public MocktailDTO(String id, String name, List<IngredientDTO> ingredients) {
        this.setId(id);
        this.setName(name);
        this.setIngredients(ingredients);
    }

    public String getId() {
        return id;
    }

    public final void setId(String id) {
        Objects.requireNonNull(id, "ID must not be null");
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        Objects.requireNonNull(name, "Name must not be null");
        this.name = name;
    }

    public List<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public final void setIngredients(List<IngredientDTO> ingredients) {
        Objects.requireNonNull(ingredients, "Ingredients must not be null");
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MocktailDTO other = (MocktailDTO) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MocktailDTO [id=" + id + ", name=" + name + ", ingredients=" + ingredients + "]";
    }

}

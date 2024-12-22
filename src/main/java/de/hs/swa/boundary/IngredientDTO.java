package de.hs.swa.boundary;

import java.util.Objects;

public class IngredientDTO {
    public String name;
    public String menge;

    public IngredientDTO() {
    }

    public IngredientDTO(String name, String menge) {
        this.setName(name);
        this.setMenge(menge);
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        Objects.requireNonNull(name, "Name must not be null");
        this.name = name;
    }

    public String getMenge() {
        return menge;
    }

    public final void setMenge(String menge) {
        Objects.requireNonNull(menge, "Menge must not be null");
        this.menge = menge;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IngredientDTO other = (IngredientDTO) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "IngredientDTO [name=" + name + ", menge=" + menge + "]";
    }
}

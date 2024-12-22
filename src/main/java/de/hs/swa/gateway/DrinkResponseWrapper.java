package de.hs.swa.gateway;

import de.hs.swa.boundary.DrinkDTO;
import java.util.List;

public class DrinkResponseWrapper {
    private List<DrinkDTO> drinks;

    public List<DrinkDTO> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<DrinkDTO> drinks) {
        this.drinks = drinks;
    }
}
